import api.naver.BookCatalogResult;
import api.naver.BookResult;
import api.naver.BookSearchResult;
import api.naver.NaverSearching;
import com.ejjang2030.bookcontentparser.BookContentParser;
import com.ejjang2030.bookcontentparser.api.kakao.KakaoSearchDocsResult;
import com.ejjang2030.bookcontentparser.api.kakao.KakaoSearchResult;
import com.ejjang2030.bookcontentparser.api.kakao.KakaoSearching;
import com.ejjang2030.bookcontentparser.api.naver.BookCatalog;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Scanner;

public class Main {
    static public void main(String[] args) {
        Main m = new Main();
        m.testNaverSearching();
    }

    void testNaverSearching() {
        final NaverSearching naver = new NaverSearching(SecretId.INSTANCE.getNAVER_CLIENT_ID(), SecretId.INSTANCE.getNAVER_CLIENT_ID_SECRET());
        Scanner scanner = new Scanner(System.in);
        System.out.print("검색하고자 하는 책 키워드를 입력하세요 : ");
        String query = scanner.nextLine();
        System.out.print("표시하고자 하는 책 수를 입력하세요 : ");
        int display = scanner.nextInt();
        System.out.print("어디서부터 찾고 싶으세요? : ");
        int start = scanner.nextInt();
        naver.searchBook(
                query,
                display,
                start,
                "sim",
                new Function3<Call<BookSearchResult>, Response<BookSearchResult>, Throwable, Unit>() {
                    @Override
                    public Unit invoke(Call<BookSearchResult> call, Response<BookSearchResult> res, Throwable t) {
                        if(res != null) {
                            if(res.isSuccessful()) {
                                BookSearchResult result = res.body();
                                if(result != null) {
                                    for(int i = 0; i < result.getItems().size(); i++) {
                                        System.out.println(i + "번 " + result.getItems().get(i).getTitle());
                                    }
                                    System.out.print("책 번호를 입력하세요 : ");
                                    Scanner sc = new Scanner(System.in);
                                    int number = sc.nextInt();
                                    final BookResult book = result.getItems().get(number);
                                    naver.getBookCatalog(
                                            book,
                                            new Function5<BookResult, BookCatalog, Call<ResponseBody>, Response<ResponseBody>, Throwable, Unit>() {
                                                @Override
                                                public Unit invoke(
                                                    BookResult bookCatalogResult,
                                                    BookCatalog bookCatalog,
                                                    Call<ResponseBody> responseBodyCall,
                                                    Response<ResponseBody> responseBodyResponse,
                                                    Throwable throwable
                                                ) {
                                                    if(bookCatalog != null) {
                                                        String content = bookCatalog.getContentsHtml();
                                                        System.out.println("content : " + content);
                                                        List<String> list = bookCatalog.getBookContentTableList();
                                                        List<String> contentList = BookContentParser.INSTANCE.getBookContentTableList(content);
                                                        ContentTreeParser tree = new ContentTreeParser(list);
                                                        System.out.println("tree : \n" + tree);
                                                    }
                                                    return Unit.INSTANCE;
                                                }
                                            });
                                }
                            } else {
                                System.out.println(res.message());
                            }

                        }
                        return Unit.INSTANCE;
                    }
                }
        );
    }

    void testKakaoSearching() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("검색하고자 하는 책 키워드를 입력하세요 : ");
        String query = scanner.nextLine();
        System.out.print("표시하고자 하는 책 수를 입력하세요 : ");
        int display = scanner.nextInt();
        System.out.print("어디서부터 찾고 싶으세요? : ");
        int start = scanner.nextInt();
        final KakaoSearching kakao = new KakaoSearching(SecretId.INSTANCE.getKAKAO_REST_API_KEY());
        kakao.searchBook(query, "latest", start, display, null,
                new Function3<Call<KakaoSearchResult.KakaoBookSearchResult>, Response<KakaoSearchResult.KakaoBookSearchResult>, Throwable, Unit>() {
                    @Override
                    public Unit invoke(Call<KakaoSearchResult.KakaoBookSearchResult> call, Response<KakaoSearchResult.KakaoBookSearchResult> res, Throwable t) {
                        if(res != null) {
                            if(res.isSuccessful()) {
                                KakaoSearchResult.KakaoBookSearchResult result = res.body();
                                if(result != null) {
                                    List<KakaoSearchDocsResult.KakaoSearchDocsBookResult> books = result.getDocuments();
                                    for(int i = 0; i < books.size(); i++) {
                                        System.out.println(i + "번 " + books.get(i).getTitle());
                                    }
                                    System.out.print("책 번호를 입력하세요 : ");
                                    Scanner sc = new Scanner(System.in);
                                    int number = sc.nextInt();
                                    KakaoSearchDocsResult.KakaoSearchDocsBookResult book = books.get(number);
                                    System.out.println(book);
                                }
                            }
                        } else {
                            System.out.println(t.getMessage());
                        }
                        return Unit.INSTANCE;
                    }
                }
        );
    }
}
