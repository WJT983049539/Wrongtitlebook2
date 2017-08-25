package xinshuyuan.com.wrongtitlebook.Model.Common;

/**
 * 储存教材 的id 和 名字
 * Created by Administrator on 2017/5/25.
 */

public class BookInfo {
    String BookId;

    String BookName;

    public String getBookName() {
        return BookName;
    }


    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public void setBookId(String bookId) {
        BookId = bookId;
    }
    public String getBookId() {
        return BookId;
    }
}
