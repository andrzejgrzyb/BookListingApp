package pl.com.andrzejgrzyb.booklistingapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andrzej on 19.07.2017.
 */

public class BookArrayAdapter extends ArrayAdapter<Book> {

    public BookArrayAdapter(Context context, ArrayList<Book> bookArrayList) {
        super(context, 0, bookArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Book book = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_layout, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.title);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.author);

        titleTextView.setText(book.getTitle());
        authorTextView.setText(TextUtils.join(", ", book.getAuthors()));

        return convertView;
    }
}