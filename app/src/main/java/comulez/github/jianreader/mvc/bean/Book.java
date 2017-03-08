package comulez.github.jianreader.mvc.bean;

import android.text.TextUtils;

/**
 * Created by Ulez on 2017/3/3.
 * Email：lcy1532110757@gmail.com
 */

public class Book extends Base {
    public static final int Type_normal = 0;
    public static final int Type_Hot = 1;
    public static final int Type_sort = 2;
    private String dec;
    private String author;
    private String image_url;
    private int type = Type_normal;

    public Book(String url, String name) {
        super(url, name);
        if (!TextUtils.isEmpty(url) && (url.contains("class") || url.contains("top"))) {
            type = Type_sort;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Book(String url, String image_url, String name, String dec, String author) {
        super(url, name);
        this.image_url = image_url;
        this.dec = dec;
        this.author = author;
        if (!TextUtils.isEmpty(image_url)) {
            type = Type_Hot;
        }
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


}
