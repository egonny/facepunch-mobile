package com.egonny.facepunch.util;

import com.egonny.facepunch.model.facepunch.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FPParser {

	private static Pattern subforumIdPattern = Pattern.compile("(?<=forumdisplay\\.php\\?f=)\\d+");
	private static Pattern subforumPagecountPattern = Pattern.compile("(?<=&page=)\\d+(?=&)");

	private static Pattern threadIdPattern = Pattern.compile("(?<=showthread\\.php\\?t=)\\d+");
	private static Pattern userIdPattern = Pattern.compile("(?<=member\\.php\\?u=)\\d+");

	private static Pattern postcountPattern = Pattern.compile("(\\d|,)+");

	private static Pattern loginRetryPattern = Pattern.compile("(?<=You have used )\\d");

	public static List<Category> parseCategories(String html) {
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("table.forums");
		List<Category> categories = new ArrayList<Category>();

		for (Element element: elements) {
			// Parse category title
			String title = element.select("tr.forumhead td h2 a").text();

			// Get subforums from category
			List<Subforum> subforums = parseSubforums(element.html());
			Category category = new Category(title);
			for (Subforum subforum: subforums) {
				category.addSubforum(subforum);
			}
			categories.add(category);
		}
		return categories;
	}

	public static List<Subforum> parseSubforums(String html) {
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("div.forumdata");
		List<Subforum> subforums = new ArrayList<Subforum>();

		for (Element element: elements) {
			// Parse subforum title
			String title = element.select("h2.forumtitle a").text();

			// Parse subforum id
			String idText = element.select("h2.forumtitle a").attr("href");
			Matcher idMatcher = subforumIdPattern.matcher(idText);
			if (idMatcher.find()) {
				int id = Integer.parseInt(idMatcher.group());
				subforums.add(new Subforum(title, id));
			}
		}
		return subforums;
	}

	public static int getSubforumPagecount(String html) {
		Document doc = Jsoup.parse(html);
		String pagecountLink = doc.select("span.first_last").attr("href");
		Matcher pagecountMatcher = subforumPagecountPattern.matcher(pagecountLink);
		if (pagecountMatcher.find()) return Integer.parseInt(pagecountMatcher.group());
		else return 1;
	}

	public static List<FPThread> parseThreads(String html) {
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("tr.threadbit");
		List<FPThread> threads = new ArrayList<FPThread>();

		for (Element element: elements) {
			// Parse thread title
			String title = element.select("h3.threadtitle a.title").text();

			// Parse thread id
			String idText = element.select("h3.threadtitle a.title").attr("href");
			Matcher idMatcher = threadIdPattern.matcher(idText);
			long id = -1;
			if (idMatcher.find()) {
				id = Long.parseLong(idMatcher.group());
			}

			// Parse thread author
			String name = element.select("div.author a").text();
			String authorIdText = element.select("div.author a").attr("href");
			Matcher authorIdMatcher = userIdPattern.matcher(authorIdText);
			long authorId = -1;
			if (authorIdMatcher.find()) {
				authorId = Long.parseLong(authorIdMatcher.group());
			}
			User author = new User(name, authorId);

			// Create Thread object
			FPThread thread = new FPThread(title, id, author);

			// Parse thread statuses
			Set<String> classnames = element.classNames();
			thread.setLocked(classnames.contains("lock"));
			thread.setOld(classnames.contains("old"));
			thread.setSticky(classnames.contains("sticky"));

			// Parse number of pages
			if (element.select("span.threadpagenav").size() != 0) {
				String page = element.select("span.threadpagenav a:contains(Last)").text();
				thread.setPages(Integer.parseInt(page.split(" ")[0]));
			}

			// Parse number of people reading
			if (element.select("span.viewers").size() != 0) {
				thread.setReading(Integer.parseInt(element.select("span.viewers").text().split(" ")[0]));
			}

			// Parse last post author
			String lastPostName = element.select("td.threadlastpost dl dd a:not(.lastpostdate)").text();
			String lastPostIdText =  element.select("td.threadlastpost dl dd a:not(.lastpostdate)").attr("href");
			Matcher lastPostIdMatcher = userIdPattern.matcher(lastPostIdText);
			long lastPostId = -1;
			if (lastPostIdMatcher.find()) {
				lastPostId = Long.parseLong(lastPostIdMatcher.group());
			}
			User lastPostUser = new User(lastPostName, lastPostId);
			thread.setLastPostAuthor(lastPostUser);

			// Parse last post date
			String lastPostDate = element.select("td.threadlastpost dl dd:not(dd:contains(by))").text();
			thread.setLastPostDate(lastPostDate);

			threads.add(thread);
		}
		return threads;
	}

	public static List<FPPost> parsePosts(String html) {
		List<FPPost> posts = new ArrayList<FPPost>();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("li.postbitlegacy");
		for (Element element: elements) {
			long id = Long.parseLong(element.attr("id").substring(5));
			String postDate = element.select("span.date").first().text();

			// Parse author
			String authorName = element.select("div.username_container").text();
			String authorIdText = element.select("div.username_container a").attr("href");
			Matcher authorIdMatcher = userIdPattern.matcher(authorIdText);
			long authorId = -1;
			if (authorIdMatcher.find()) {
				authorId = Long.parseLong(authorIdMatcher.group());
			}
			String userGroupColor = element.select("div.username_container a font").attr("color");
			// Moderator check
			if (userGroupColor.equals("") && !element.select("div.username_container a span").attr("style").equals("")) {
				userGroupColor = "#00aa00";
			}
			String postcountText = element.select("div#userstats").first().childNode(2).toString();
			Matcher postcountMatcher = postcountPattern.matcher(postcountText);
			int postcount = -1;
			if (postcountMatcher.find()) {
					postcount = Integer.parseInt(postcountMatcher.group().replace(",", ""));
			}
			String joinDate = element.select("div#userstats").first().childNode(0).toString().substring(2);

			User author = new User(authorName, authorId);
			author.setJoinDate(joinDate);
			author.setPostcount(postcount);
			author.setUserGroup(userGroupColor);
			FPPost post = new FPPost(id, author, postDate);

			String message = element.select("blockquote.postcontent").html();
			post.setMessage(message);
			boolean old = element.hasClass("postbitold");
			post.setOld(old);

			// Parse ratings
			Map<String, Integer> ratingsMap = new HashMap<String, Integer>();
			for (Element ratingElement: element.select("span.rating_results span")) {
				String rating = ratingElement.select("img").attr("alt");
				int ratingAmount = Integer.parseInt(ratingElement.select("strong").text());
				ratingsMap.put(rating, ratingAmount);
			}
			post.setRatings(ratingsMap);

			//TODO: Parse rating keys

			posts.add(post);

		}
		return posts;
	}

	public static LoginResponse parseLogin(String html) {
		Document doc = Jsoup.parse(html);
		Element response = doc.getElementsByClass("restore").get(0);
		LoginResponse loginResponse = new LoginResponse();
		if (response.text().contains("You have entered an invalid username or password")) {
			loginResponse.error = Error.INCORRECT_USERNAME;
			Matcher m = loginRetryPattern.matcher(response.text());
			if (m.find()) {
				loginResponse.retry = Integer.parseInt(m.group(0));
			} else {
				loginResponse.retry = -1;
			}
		} else if (response.text().contains("failed login quota!")) {
			loginResponse.error = Error.RETRIES_LIMIT_REACHED;
		} else {
			loginResponse.username = response.text().substring(26);
		}
		return loginResponse;
	}

	public static class LoginResponse {
		public Error error;
		public String username;
		public int retry;
	}
	public enum Error {
		INCORRECT_USERNAME,
		RETRIES_LIMIT_REACHED
	}
}
