package comulez.github.jianreader.mvc.bean;

/**
 * Created by eado on 2017/3/16.
 */

public class BookDetail {
    String bookName;
    String author;
    String type;
    String status;
    String up_date;
    String latestChapter;
    String latestUrl;
    String intro;
    String image_cover;

    public BookDetail(String bookName, String author, String type, String status, String up_date, String latestChapter, String latestUrl, String intro, String image_cover) {
        this.bookName = bookName;
        this.author = author;
        this.type = type;
        this.status = status;
        this.up_date = up_date;
        this.latestChapter = latestChapter;
        this.latestUrl = latestUrl;
        this.intro = intro;
        this.image_cover = image_cover;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImage_cover() {
        return image_cover;
    }

    public void setImage_cover(String image_cover) {
        this.image_cover = image_cover;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getUp_date() {
        return up_date;
    }

    public String getLatestChapter() {
        return latestChapter;
    }

    public String getLatestUrl() {
        return latestUrl;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUp_date(String up_date) {
        this.up_date = up_date;
    }

    public void setLatestChapter(String latestChapter) {
        this.latestChapter = latestChapter;
    }

    public void setLatestUrl(String latestUrl) {
        this.latestUrl = latestUrl;
    }
}
