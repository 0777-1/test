import java.util.List;

/**
 * T1 搜索修复后的测试：书名包含关键字即命中（区分大小写，只匹配书名、不匹配作者）。
 *
 * 约定：类名以 Test 结尾；测试方法为无参 public void 且以 test 开头；使用 Assert 断言。
 * 直接 new Library(true) 调用，不依赖控制台。
 */
public class SearchTest {

    /** "Java" 应命中 1 本《Java入门》。 */
    public void testSearchJavaPrefix() {
        Library library = new Library(true);
        List<Book> books = library.search("Java");
        Assert.assertEquals(1, books.size(), "搜索 Java 应命中 1 本");
        Assert.assertEquals("Java入门", books.get(0).getName(), "命中的应为《Java入门》");
    }

    /** "入门" 应命中 1 本（关键字在书名尾部）。 */
    public void testSearchSuffix() {
        Library library = new Library(true);
        List<Book> books = library.search("入门");
        Assert.assertEquals(1, books.size(), "搜索 入门 应命中 1 本");
        Assert.assertEquals(1, books.get(0).getId(), "命中的应为编号 1");
    }

    /** "张" 是作者姓氏，书名中不含，应命中 0 本（只匹配书名）。 */
    public void testSearchAuthorKeywordNoHit() {
        Library library = new Library(true);
        Assert.assertEquals(0, library.search("张").size(), "搜索 张 应命中 0 本（不匹配作者）");
    }

    /** "java" 与书名 "Java入门" 大小写不一致，应命中 0 本（区分大小写）。 */
    public void testSearchLowerCaseNoHit() {
        Library library = new Library(true);
        Assert.assertEquals(0, library.search("java").size(), "搜索 java 应区分大小写，命中 0 本");
    }

    /** "数据结构" 完整书名应命中 1 本。 */
    public void testSearchFullName() {
        Library library = new Library(true);
        List<Book> books = library.search("数据结构");
        Assert.assertEquals(1, books.size(), "搜索 数据结构 应命中 1 本");
        Assert.assertEquals(2, books.get(0).getId(), "命中的应为编号 2");
    }

    /** 完整书名仍应命中：修复后是原精确匹配的超集，原有行为不丢。 */
    public void testSearchExactFullNameStillHits() {
        Library library = new Library(true);
        Assert.assertEquals(1, library.search("Java入门").size(), "完整书名应命中 1 本");
        Assert.assertEquals(1, library.search("算法导论").size(), "完整书名应命中 1 本");
    }

    /** 关键字在书名中段也应命中。 */
    public void testSearchMiddleSegment() {
        Library library = new Library(true);
        Assert.assertEquals(1, library.search("据结").size(), "搜索 据结 应命中《数据结构》");
    }

    /** 命中多本时应全部返回。 */
    public void testSearchReturnsAllMatches() {
        Library library = new Library(true);
        library.add("Java进阶", "赵六", 80.0);
        List<Book> books = library.search("Java");
        Assert.assertEquals(2, books.size(), "两本 Java 相关图书都应命中");
        Assert.assertEquals(1, books.get(0).getId(), "第一本应为编号 1");
        Assert.assertEquals(4, books.get(1).getId(), "第二本应为新增的编号 4");
    }

    /** 无匹配关键字返回空列表。 */
    public void testSearchNoMatch() {
        Library library = new Library(true);
        Assert.assertEquals(0, library.search("不存在的书").size(), "无关关键字应命中 0 本");
    }
}
