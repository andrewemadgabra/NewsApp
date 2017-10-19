package com.example.andrew.newsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Activity context, ArrayList<News> NEWS) {
        super(context, 0, NEWS);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_list, parent, false);
        }

        News currentAndroid = getItem(position);
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(currentAndroid.getTitle());
        TextView type = (TextView) listItemView.findViewById(R.id.type);
        type.setText(currentAndroid.gettype());
        TextView web = (TextView) listItemView.findViewById(R.id.weburl);
        web.setText(currentAndroid.getWeburl());
        TextView sectionname = (TextView) listItemView.findViewById(R.id.sectionname);
        sectionname.setText(currentAndroid.getSectionName());
        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(currentAndroid.getDate());
        TextView article = (TextView) listItemView.findViewById(R.id.article);
        article.setText(currentAndroid.getArticle());
        return listItemView;
    }
}
