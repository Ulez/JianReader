package comulez.github.jianreader.mvc.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ulez on 2017/2/23.
 * Email：lcy1532110757@gmail.com
 */

public class RankBean implements Parcelable {
    private String url;
    private String rankName;

    public RankBean(String url, String rankName) {
        this.url = url;
        this.rankName = rankName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    @Override
    public String toString() {
        return "RankBean{" +
                "url='" + url + '\'' +
                ", rankName='" + rankName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.rankName);
    }

    protected RankBean(Parcel in) {
        this.url = in.readString();
        this.rankName = in.readString();
    }

    public static final Parcelable.Creator<RankBean> CREATOR = new Parcelable.Creator<RankBean>() {
        @Override
        public RankBean createFromParcel(Parcel source) {
            return new RankBean(source);
        }

        @Override
        public RankBean[] newArray(int size) {
            return new RankBean[size];
        }
    };
}
