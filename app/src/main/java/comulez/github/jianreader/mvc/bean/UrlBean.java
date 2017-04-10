package comulez.github.jianreader.mvc.bean;

/**
 * Created by Ulez on 2017/4/7.
 * Email：lcy1532110757@gmail.com
 */


public class UrlBean {
    private String bookName;
    private String chapterName;
    private String url;
    private String next_url;
    private String pre_url;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public UrlBean(String bookName, String chapterName, String url, String next_url, String pre_url) {
        this.bookName = bookName;
        this.chapterName = chapterName;
        this.url = url;
        this.next_url = next_url;
        this.pre_url = pre_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public String getPre_url() {
        return pre_url;
    }

    public void setPre_url(String pre_url) {
        this.pre_url = pre_url;
    }
}
