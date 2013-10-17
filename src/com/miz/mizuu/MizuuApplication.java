package com.miz.mizuu;

import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;

import com.crashlytics.android.Crashlytics;
import com.miz.functions.CifsImageDownloader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;

public class MizuuApplication extends Application {

	private static DbAdapterTvShow dbTvShow;
	private static DbAdapterTvShowEpisode dbTvShowEpisode;
	private static DbAdapterSources dbSources;
	private static DbAdapter db;
	private static HashMap<String, String[]> map = new HashMap<String, String[]>();

	@Override
	public void onCreate() {
		super.onCreate();

		jcifs.Config.setProperty("jcifs.smb.client.disablePlainTextPasswords", "false");

		if (!(0 != ( getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)))
			Crashlytics.start(this);

		initImageLoader(getApplicationContext());

		// Database setup
		dbTvShow = new DbAdapterTvShow(this);
		dbTvShowEpisode = new DbAdapterTvShowEpisode(this);
		dbSources = new DbAdapterSources(this);
		db = new DbAdapter(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		dbTvShow.close();
		dbTvShowEpisode.close();
		dbSources.close();
		db.close();
	}

	public static void initImageLoader(Context context) {

		if (!(0 != ( context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)))
			L.disableLogging();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPoolSize(2)
		.threadPriority(Thread.NORM_PRIORITY - 1)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheSize(100 * 1024 * 1024)
		.discCacheFileCount(200)
		.imageDownloader(new CifsImageDownloader(context))
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.FIFO)
		.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static DisplayImageOptions getDefaultCoverLoadingOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.loading_image)
		.showImageOnFail(R.drawable.loading_image)
		.cacheInMemory(true)
		.showImageForEmptyUri(R.drawable.loading_image)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		return options;
	}

	public static DisplayImageOptions getDefaultActorLoadingOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.noactor)
		.showImageOnFail(R.drawable.noactor)
		.showImageForEmptyUri(R.drawable.noactor)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		return options;
	}

	public static DisplayImageOptions getDefaultBackdropLoadingOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.nobackdrop)
		.showImageOnFail(R.drawable.nobackdrop)
		.showImageForEmptyUri(R.drawable.nobackdrop)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		return options;
	}
	
	public static DisplayImageOptions getBackdropLoadingOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnFail(R.drawable.bg)
		.cacheInMemory(false)
		.showImageForEmptyUri(R.drawable.bg)
		.cacheOnDisc(false)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		return options;
	}

	public static DbAdapterTvShow getTvDbAdapter() {
		return dbTvShow;
	}

	public static DbAdapterTvShowEpisode getTvEpisodeDbAdapter() {
		return dbTvShowEpisode;
	}

	public static DbAdapterSources getSourcesAdapter() {
		return dbSources;
	}

	public static DbAdapter getMovieAdapter() {
		return db;
	}

	public static String[] getCifsFilesList(String parentPath) {
		return map.get(parentPath);
	}

	public static void putCifsFilesList(String parentPath, String[] list) {
		if (!map.containsKey(parentPath))
			map.put(parentPath, list);
	}
}