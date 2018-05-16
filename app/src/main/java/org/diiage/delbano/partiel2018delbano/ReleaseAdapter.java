package org.diiage.delbano.partiel2018delbano;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bastien on 16/05/2018.
 */

public class ReleaseAdapter extends BaseAdapter {
    ArrayList<Release> releases;
    Activity activity;

    public ReleaseAdapter (Activity activity, ArrayList<Release> releases){
        this.activity = activity;
        this.releases = releases;
    }

    @Override
    public int getCount() {
        return releases.size();
    }

    @Override
    public Object getItem(int i) {
        return releases.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        LayoutInflater inflater = this.activity.getLayoutInflater();
        if (view == null){
            view = inflater.inflate(R.layout.item_main, viewGroup, false);

            holder = new ViewHolder();
            holder.txtReleaseTitle = (TextView)view.findViewById(R.id.txtReleaseTitle);

            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        Release release = (Release) getItem(i);

        holder.txtReleaseTitle.setText(String.valueOf(release.getTitle()));

        return view;
    }

    private static class ViewHolder {
        public TextView txtReleaseTitle;

    }
}
