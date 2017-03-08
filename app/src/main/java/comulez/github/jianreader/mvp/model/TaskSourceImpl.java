package comulez.github.jianreader.mvp.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import comulez.github.jianreader.mvc.bean.Book;

/**
 * Created by Ulez on 2017/3/8.
 * Email：lcy1532110757@gmail.com
 */

public class TaskSourceImpl implements TaskSource {
    @Override
    public ArrayList<Book> getDataFromServer(String url_sss) {
        ArrayList<Book> books = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url_sss).get();
            Elements hotBook = doc.select("div.block");
            for (int i = 0; i < hotBook.size(); i++) {
                Element els = hotBook.get(i);
                String url = els.select("a").get(1).attr("href");//带图片、简介的小说；
                String bookName = els.select("a").get(2).text();
                String author = els.select("a").get(3).text();
                String dec = els.select("a").get(4).text();
                Elements image = els.select("div.block_img");
                String image_url = image.select("img").first().attr("src");
                books.add(new Book(url, image_url, bookName, dec, author));
                Elements normalBook = els.select("ul").select("li");
                for (int j = 0; j < normalBook.size(); j++) {
                    Element xxx = normalBook.get(j);
                    Elements book = xxx.select("a.blue");
                    String bookUrl = book.select("a").first().attr("href");
                    String bookName2 = book.text();
                    String author2 = xxx.select("a").last().text();
                    books.add(new Book(bookUrl, null, bookName2, null, author2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return books;
        }
    }

    @Override
    public ArrayList<Book> getDataFromCache(String url) {
        return null;
    }
}
