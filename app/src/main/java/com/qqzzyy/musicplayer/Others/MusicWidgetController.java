package com.qqzzyy.musicplayer.Others;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.qqzzyy.musicplayer.R;

/**
 * Implementation of App Widget functionality.
 */
public class MusicWidgetController extends AppWidgetProvider {

    private static RemoteViews contentViews;

    private static final String ACTION_NEXT_SONG = "action.nextsong";
    private static final String ACTION_PAUSE = "action.pause";
    private static final String ACTION_PRE_SONG = "action.presong";
    private static final String ACTION_PLAY_SONG = "action.playsong";
    private static final String ACTION_CANCEL = "action.cancel";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        contentViews = new RemoteViews(context.getPackageName(), R.layout.notification_control);


        //上一首图标添加点击监听
        Intent previousButtonIntent = new Intent(ACTION_PRE_SONG);
        PendingIntent pendPreviousButtonIntent = PendingIntent.getBroadcast(context, 0, previousButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_privious, pendPreviousButtonIntent);
        //播放添加点击监听
        Intent playButtonIntent = new Intent(ACTION_PLAY_SONG);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, playButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_play, playPendingIntent);
        //播放添加点击监听
        Intent pauseButtonIntent = new Intent(ACTION_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(context, 0, pauseButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_pause, pausePendingIntent);
        //下一首图标添加监听
        Intent nextButtonIntent = new Intent(ACTION_NEXT_SONG);
        PendingIntent pendNextButtonIntent = PendingIntent.getBroadcast(context, 0, nextButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_next, pendNextButtonIntent);
        //退出监听
        Intent exitButton = new Intent(ACTION_CANCEL);
        PendingIntent pendingExitButtonIntent = PendingIntent.getBroadcast(context,0,exitButton,0);
        contentViews.setOnClickPendingIntent(R.id.notification_cancel,pendingExitButtonIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, contentViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

