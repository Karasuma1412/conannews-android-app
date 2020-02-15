package de.karasuma.android.conannews.communication;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

import de.karasuma.android.conannews.MainActivity;
import de.karasuma.android.conannews.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        String summary = remoteMessage.getData().get("summary");

        //remove all html tags from summary
        summary = summary.replaceAll("<[^>]*>",  "");


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "conannews")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(summary))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = 123455141;
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}