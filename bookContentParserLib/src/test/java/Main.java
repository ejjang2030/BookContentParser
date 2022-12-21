import api.naver.BookCatalogResult;
import api.naver.BookResult;
import api.naver.BookSearchResult;
import api.naver.NaverSearching;
import com.ejjang2030.bookcontentparser.BookContentParser;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Set;

public class Main {
    static public void main(String[] args) {
        final NaverSearching naver = new NaverSearching(SecretId.INSTANCE.getNAVER_CLIENT_ID(), SecretId.INSTANCE.getNAVER_CLIENT_ID_SECRET());
        naver.searchBook(
                "파이썬",
                10,
                1,
                "sim",
                new Function3<Call<BookSearchResult>, Response<BookSearchResult>, Throwable, Unit>() {
                    @Override
                    public Unit invoke(Call<BookSearchResult> call, Response<BookSearchResult> res, Throwable t) {
                        if(res != null) {
                            BookSearchResult result = res.body();
                            assert result != null;
                            BookResult book = result.getItems().get(0);
                            System.out.println("book : " + book);
                            String link = book.getLink();
                            naver.getBookCatalog(
                                    book,
                                    new Function4<Call<ResponseBody>, Response<ResponseBody>, BookCatalogResult, Throwable, Unit>() {
                                        @Override
                                        public Unit invoke(Call<ResponseBody> call, Response<ResponseBody> res, BookCatalogResult catalog, Throwable t) {
                                            if(catalog != null) {
                                                String content = catalog.getDescriptions().getContentTable();
                                                List<String> contentList = BookContentParser.INSTANCE.getBookContentTableList(content);
                                                ContentTreeParser tree = new ContentTreeParser(contentList);
                                                System.out.println("tree : \n" + tree);
                                            }
                                            return Unit.INSTANCE;
                                        }
                                    });
                        }
                        return Unit.INSTANCE;
                    }
                }
        );
    }
}
