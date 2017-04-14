package comulez.github.jianreader.mvc.bean;

/**
 * Created by Ulez on 2017/4/14.
 * Email：lcy1532110757@gmail.com
 */


public class ReadBook {
    String bookUrl;//书名；
    String bookName;//书名；
    String author;//作者
    String status;//完结？
    String up_date;//最近更新；
    String latestChapter;//最新章节
    String latestUrl;//最新章节url
    String image_cover;//封面图
    String readChapter;//读到章节名；
    String chapterUrl;//读到章节url；
    String nextUrl;//下一章节
    String preUrl;//上一章节；

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public ReadBook(String bookName, String bookUrl, String author, String status, String up_date, String latestChapter, String latestUrl, String image_cover, String readChapter, String chapterUrl, String nextUrl, String preUrl) {
        this.bookName = bookName;
        this.bookUrl = bookUrl;
        this.author = author;
        this.status = status;
        this.up_date = up_date;
        this.latestChapter = latestChapter;
        this.latestUrl = latestUrl;
        this.image_cover = image_cover;
        this.readChapter = readChapter;
        this.chapterUrl = chapterUrl;
        this.nextUrl = nextUrl;
        this.preUrl = preUrl;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUp_date() {
        return up_date;
    }

    public void setUp_date(String up_date) {
        this.up_date = up_date;
    }

    public String getLatestChapter() {
        return latestChapter;
    }

    public void setLatestChapter(String latestChapter) {
        this.latestChapter = latestChapter;
    }

    public String getLatestUrl() {
        return latestUrl;
    }

    public void setLatestUrl(String latestUrl) {
        this.latestUrl = latestUrl;
    }

    public String getImage_cover() {
        return image_cover;
    }

    public void setImage_cover(String image_cover) {
        this.image_cover = image_cover;
    }

    public String getReadChapter() {
        return readChapter;
    }

    public void setReadChapter(String readChapter) {
        this.readChapter = readChapter;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getPreUrl() {
        return preUrl;
    }

    public void setPreUrl(String preUrl) {
        this.preUrl = preUrl;
    }
}
