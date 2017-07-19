package pl.com.andrzejgrzyb.booklistingapp;

import java.util.ArrayList;

/**
 * Created by Andrzej on 19.07.2017.
 */

public interface AsyncResponse {
    void processFinish(ArrayList<Book> bookArrayList);
}