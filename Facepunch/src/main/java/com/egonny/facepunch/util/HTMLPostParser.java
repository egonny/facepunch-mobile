package com.egonny.facepunch.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.egonny.facepunch.R;
import com.egonny.facepunch.views.QuoteView;
import com.egonny.facepunch.views.SoundcloudView;
import com.egonny.facepunch.views.YoutubeView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLPostParser {

	private static final Pattern youtubePattern = Pattern.compile("(?<=embed/)[^?]+");

	public static List<View> parse(LayoutInflater inflater, String message) {
		Document doc = Jsoup.parse(message);
		StringBuilder text = new StringBuilder();
		List<View> result = new ArrayList<View>();

		// Very ugly code, but it'll do until I find something better

		for (Node node: doc.body().childNodes()) {
			if (node instanceof TextNode)
				text = text.append(node.toString());
			else if (node instanceof Element) {
				Element element = (Element) node;
				View parsedView;

				if (element.tagName().equals("div")) {
					parsedView = parseDiv(element, inflater);

				} else if (element.tagName().equals("video")) {
					parsedView = parseVideo(element, inflater);

				} else if (element.tagName().equals("a") && element.childNode(0) instanceof Element
						&& ((Element)element.childNode(0)).tagName().equals("img")) {
					parsedView = parseImage((Element) element.childNode(0), inflater);

				} else if (element.tagName().equals("img")) {
					parsedView = parseImage(element, inflater);

				} else {
					text = text.append(element.toString());
					continue;
				}

				View textView = parseText(text.toString(), inflater);
				text.delete(0, text.length());
				if (textView != null) result.add(textView);
				result.add(parsedView);
			}
		}
		View textView = parseText(text.toString(), inflater);
		if (textView != null) result.add(textView);
		return result;
	}

	private static View parseText(String text, LayoutInflater inflater) {
		if (text.length() == 0) return null;
		Spanned span = Html.fromHtml(String.valueOf(text));
		TextView view = (TextView) inflater.inflate(R.layout.thread_item_message_text, null);
		if (view == null)
			throw new IllegalStateException("Could not find a TextView to place the parsed text.");
		view.setText(span);
		// Create hyperlinks
		Linkify.addLinks(view, Linkify.ALL);
		view.setMovementMethod(LinkMovementMethod.getInstance());
		return view;
	}

	private static View parseImage(Element element, LayoutInflater inflater) {
		final ImageView imageView = (ImageView) inflater.inflate(R.layout.thread_item_message_image, null);
		if (imageView == null) throw new IllegalStateException("Could not find an ImageView to place the parsed image.");
		String url = element.attr("src");
		if (!url.substring(0, 4).equals("http")) url = "http://www.facepunch.com" + url;
		ImageLoaderHelper.getInstance().getBitmap(url, new ImageLoaderHelper.ImageCallback() {
			@Override
			public void onResult(boolean success, Bitmap bitmap) {
				if (success && bitmap != null) imageView.setImageBitmap(bitmap);
			}
		});
		return imageView;
	}

	private static View parseVideo(Element element, LayoutInflater inflater) {
		return parseText(element.text(), inflater);
	}

	private static View parseDiv(Element element, LayoutInflater inflater) {
		if (element.hasClass("quote")) {
			return parseQuote(element, inflater);
		} else if (element.hasClass("bbcode_container")) {
			return parseCode(element, inflater);
		} else if (element.hasClass("center")) {
			return parseMedia(element, inflater);
		}
		return parseText(element.text(), inflater);
	}

	private static View parseQuote(Element element, LayoutInflater inflater) {
		QuoteView view = new QuoteView(inflater.getContext());

		int index = 0;
		String author = null;
		List<View> message = new ArrayList<View>();
		if (element.child(index).hasClass("information")) {
			author = element.child(index++).text();
			author = author.substring(0, author.indexOf(" posted:"));
		}
		View result = parseText(element.child(index).text(), inflater);
		if (result != null) message.add(result);
		view.setQuote(author, message);
		return view;
	}

	private static View parseCode(Element element, LayoutInflater inflater) {
		String text = Jsoup.parse(element.childNode(element.childNodes().size()-1).toString()).text();      // Not optimal, how do we escape HTML characters?
		TextView view = (TextView) inflater.inflate(R.layout.thread_item_message_code, null);
		view.setText(text);

		return view;
	}

	private static View parseMedia(Element element, LayoutInflater inflater) {
		if (element.child(0).hasClass("video"))
			return parseYouTube(element.child(0), inflater);
		else if (element.child(0).hasClass("media_soundcloud"))
			return parseSoundCloud(element.child(0), inflater);             //TODO placeholder
		return parseText(element.text(), inflater);
	}

	private static View parseYouTube(Element element, LayoutInflater inflater) {
		String url = element.child(0).attr("src");
		final YoutubeView view = new YoutubeView(inflater.getContext());

		// Find the ID
		final Matcher m = youtubePattern.matcher(url);

		if (m.find()) {
			final String id = m.group();
			view.load(id);
		}
		return view;
	}

	private static View parseSoundCloud(Element element, LayoutInflater inflater) {
		String url = element.child(1).child(0).attr("href");
		SoundcloudView view = new SoundcloudView(inflater.getContext());
		view.load(url);

		return view;
	}
}
