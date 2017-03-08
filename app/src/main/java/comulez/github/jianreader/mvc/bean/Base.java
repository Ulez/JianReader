package comulez.github.jianreader.mvc.bean;

/**
 * Created by Ulez on 2017/2/25.
 * Email：lcy1532110757@gmail.com
 */

public class Base {
    protected String url;
    protected String name;

    public Base(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Base{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
