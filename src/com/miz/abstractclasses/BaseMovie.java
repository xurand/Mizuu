package com.miz.abstractclasses;

import java.io.File;
import java.util.Locale;

import com.miz.functions.MizLib;

import android.content.Context;

public abstract class BaseMovie implements Comparable<BaseMovie> {

	protected Context CONTEXT;
	protected String ROW_ID, FILEPATH, TITLE, TMDB_ID;
	protected boolean ignorePrefixes, ignoreNfo;

	public String getRowId() {
		return ROW_ID;
	}

	public String getTitle() {
		if (TITLE == null || TITLE.isEmpty()) {
			File fileName = new File(FILEPATH);
			return fileName.getName().substring(0, fileName.getName().lastIndexOf("."));
		} else {
			if (ignorePrefixes) {
				String temp = TITLE.toLowerCase(Locale.ENGLISH);
				String[] prefixes = MizLib.getPrefixes(CONTEXT);
				int count = prefixes.length;
				for (int i = 0; i < count; i++) {
					if (temp.startsWith(prefixes[i]))
						return TITLE.substring(prefixes[i].length());
				}
			}
			return TITLE;
		}
	}

	/**
	 * This is only used for the SectionIndexer in the overview
	 */
	@Override
	public String toString() {
		try {
			return getTitle().substring(0, 1);
		} catch (Exception e) {
			return "";
		}
	}

	public String getThumbnail() {
		if (!ignoreNfo) {
			try {
				// Check if there's a custom cover art image
				String filename = FILEPATH.substring(0, FILEPATH.lastIndexOf(".")).replace("cd1", "").replace("cd2", "").replace("part1", "").replace("part2", "").trim();
				File parentFolder = new File(FILEPATH).getParentFile();

				if (parentFolder != null) {
					File[] list = parentFolder.listFiles();

					if (list != null) {
						String name, absolutePath;
						int count = list.length;
						for (int i = 0; i < count; i++) {
							name = list[i].getName();
							absolutePath = list[i].getAbsolutePath();
							if (name.equalsIgnoreCase("poster.jpg") ||
									name.equalsIgnoreCase("poster.jpeg") ||
									name.equalsIgnoreCase("poster.tbn") ||
									name.equalsIgnoreCase("folder.jpg") ||
									name.equalsIgnoreCase("folder.jpeg") ||
									name.equalsIgnoreCase("folder.tbn") ||
									name.equalsIgnoreCase("cover.jpg") ||
									name.equalsIgnoreCase("cover.jpeg") ||
									name.equalsIgnoreCase("cover.tbn") ||
									absolutePath.equalsIgnoreCase(filename + ".jpg") ||
									absolutePath.equalsIgnoreCase(filename + ".jpeg") ||
									absolutePath.equalsIgnoreCase(filename + ".tbn")) {
								return absolutePath;
							}
						}
					}
				}
			} catch (Exception e) {}
		}

		// New naming style
		return new File(MizLib.getMovieThumbFolder(CONTEXT), TMDB_ID + ".jpg").getAbsolutePath();
	}

	public String getBackdrop() {
		if (!ignoreNfo) {
			try {
				// Check if there's a custom cover art image
				String filename = FILEPATH.substring(0, FILEPATH.lastIndexOf(".")).replace("cd1", "").replace("cd2", "").replace("part1", "").replace("part2", "").trim();
				File parentFolder = new File(FILEPATH).getParentFile();

				if (parentFolder != null) {
					File[] list = parentFolder.listFiles();

					if (list != null) {
						String name, absolutePath;
						int count = list.length;
						for (int i = 0; i < count; i++) {
							name = list[i].getName();
							absolutePath = list[i].getAbsolutePath();
							if (name.equalsIgnoreCase("fanart.jpg") ||
									name.equalsIgnoreCase("fanart.jpeg") ||
									name.equalsIgnoreCase("fanart.tbn") ||
									name.equalsIgnoreCase("banner.jpg") ||
									name.equalsIgnoreCase("banner.jpeg") ||
									name.equalsIgnoreCase("banner.tbn") ||
									name.equalsIgnoreCase("backdrop.jpg") ||
									name.equalsIgnoreCase("backdrop.jpeg") ||
									name.equalsIgnoreCase("backdrop.tbn") ||
									absolutePath.equalsIgnoreCase(filename + "-fanart.jpg") ||
									absolutePath.equalsIgnoreCase(filename + "-fanart.jpeg") ||
									absolutePath.equalsIgnoreCase(filename + "-fanart.tbn") ||
									absolutePath.equalsIgnoreCase(filename + "-banner.jpg") ||
									absolutePath.equalsIgnoreCase(filename + "-banner.jpeg") ||
									absolutePath.equalsIgnoreCase(filename + "-banner.tbn") ||
									absolutePath.equalsIgnoreCase(filename + "-backdrop.jpg") ||
									absolutePath.equalsIgnoreCase(filename + "-backdrop.jpeg") ||
									absolutePath.equalsIgnoreCase(filename + "-backdrop.tbn")) {
								return absolutePath;
							}
						}
					}
				}
			} catch (Exception e) {}
		}

		// New naming style
		return new File(MizLib.getMovieBackdropFolder(CONTEXT), TMDB_ID + "_bg.jpg").getAbsolutePath();
	}

	@Override
	public int compareTo(BaseMovie another) {
		return getTitle().compareToIgnoreCase(another.getTitle());
	}

}