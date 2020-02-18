package de.karasuma.android.conannews;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;

import de.karasuma.android.conannews.activities.ConanCastActivity;

public class ConanCastAdapter extends ArrayAdapter<File> {

    private String TAG = "ConanCastAdapter";

    public ConanCastAdapter(ConanCastActivity conanCastActivity, ArrayList<File> fileList) {
        super(conanCastActivity, 0, fileList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final File file = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.conancast_item, parent, false);
        }

        TextView conanCastFileTitleView = convertView.findViewById(R.id.conancast_file_title);

        assert file != null;
        conanCastFileTitleView.setText(file.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "clicked on file " + file.getName());
                String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(FileProvider
                                .getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file),
                        mimetype);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
