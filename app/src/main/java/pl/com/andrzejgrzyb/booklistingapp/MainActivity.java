package pl.com.andrzejgrzyb.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.no_books_textview)
    TextView noBooksTextView;

    private ArrayList<Book> mArrayList;
    private BookArrayAdapter mBookArrayAdapter;
    private BookSearchAsyncTask mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        listView.setEmptyView(noBooksTextView);
        mArrayList = new ArrayList<>();
        mBookArrayAdapter = new BookArrayAdapter(this, mArrayList);
        listView.setAdapter(mBookArrayAdapter);


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(getString(R.string.saved_instance_book_list))) {
                mArrayList = savedInstanceState
                        .getParcelableArrayList(getString(R.string.saved_instance_book_list));
                updateBookList();
                editText.clearFocus();
            }
            if (mArrayList.size() == 0) {
                setEmptyView();
            } else {
                listView.setAdapter(mBookArrayAdapter);
            }
        }
    }

    private void updateBookList() {
        mBookArrayAdapter = new BookArrayAdapter(getBaseContext(), mArrayList);
        listView.setAdapter(mBookArrayAdapter);
        mBookArrayAdapter.notifyDataSetChanged();
    }


    private void setEmptyView() {
        noBooksTextView.setText(getResources().getString(R.string.no_books));
        listView.setEmptyView(findViewById(R.id.no_books_textview));
    }

    @OnClick(R.id.button)
    public void onClick(View view) {

        editText.clearFocus();

        String searchQuery = editText.getText().toString().replaceAll(" ", "+");

        if (searchQuery != null && !searchQuery.equals("")) {

            String searchString = getString(R.string.google_api_string) + searchQuery;

            if (isNetworkAvailable(this)) {
                mAsyncTask = new BookSearchAsyncTask(new AsyncResponse() {
                    @Override
                    public void processFinish(ArrayList<Book> bookArrayList) {
                        if (bookArrayList != null) {
                            mBookArrayAdapter.clear();
                            mBookArrayAdapter.addAll(bookArrayList);
                            mBookArrayAdapter.notifyDataSetChanged();
                        } else {
                            setEmptyView();
                        }
                    }
                });
                mAsyncTask.execute(searchString);
            } else {
                setEmptyView();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.enter_query), Toast.LENGTH_SHORT).show();
        }


    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(getResources().getString(R.string.saved_instance_book_list), mArrayList);
        super.onSaveInstanceState(savedInstanceState);
    }
}