import java.util.ArrayList;
import java.util.List;

/**
 * 图书管理核心逻辑：内存 ArrayList 存放图书，不依赖任何控制台 IO，便于测试。
 *
 * 消息文案统一由本类返回，Main 只负责打印。
 */
public class Library {

    private final List<Book> books = new ArrayList<>();
    private int nextId = 1;

    /**
     * @param withSeedData 是否预置 3 本演示图书（编号 1-3，之后 nextId 从 4 开始）
     */
    public Library(boolean withSeedData) {
        if (withSeedData) {
            add("Java入门", "张三", 59.5);
            add("数据结构", "李四", 45.0);
            add("算法导论", "王五", 88.0);
            // 第二本预置为已借出状态
            findById(2).setBorrowed(true);
        }
    }

    /**
     * 添加图书，分配自增编号（从当前 nextId 开始，只增不减；删除图书不复用编号）。
     *
     * @return 新建的图书对象
     */
    public Book add(String name, String author, double price) {
        Book book = new Book(nextId++, name, author, price);
        books.add(book);
        return book;
    }

    /** 全部图书（按添加顺序，返回副本） */
    public List<Book> listAll() {
        return new ArrayList<>(books);
    }

    /**
     * 按书名关键字搜索，返回书名包含该关键字的所有图书。
     *
     * @param keyword 关键字（区分大小写；只匹配书名，不匹配作者）
     */
    public List<Book> search(String keyword) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            // 书名包含关键字即命中（部分匹配，如输入 "Java"、"入门" 均可命中《Java入门》），区分大小写。
            if (book.getName().contains(keyword)) {
                result.add(book);
            }
        }
        return result;
    }

    /** 按编号查找图书，不存在返回 null */
    public Book findById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    /** 借阅：未借出 -> 已借出 */
    public String borrow(int id) {
        Book book = findById(id);
        if (book == null) {
            return "图书编号不存在";
        }
        if (book.isBorrowed()) {
            return "该图书已被借出";
        }
        book.setBorrowed(true);
        return "借阅成功";
    }

    /** 归还：已借出 -> 未借出 */
    public String returnBook(int id) {
        Book book = findById(id);
        if (book == null) {
            return "图书编号不存在";
        }
        if (!book.isBorrowed()) {
            return "该图书未被借出";
        }
        book.setBorrowed(false);
        return "归还成功";
    }

    /**
     * 借阅 / 归还（切换语义）：未借出则借阅，已借出则归还；编号不存在返回不存在提示。
     *
     * 切换判断放在本类，Main 只负责打印返回的文案（消息文案统一由本类返回）。
     */
    public String borrowOrReturn(int id) {
        Book book = findById(id);
        if (book == null) {
            return "图书编号不存在";
        }
        return book.isBorrowed() ? returnBook(id) : borrow(id);
    }

    /**
     * 删除：编号存在则从书库移除并返回成功提示；不存在（含 0、负数、超出范围的编号）返回不存在提示。
     *
     * 编号只会从剩余图书中查找，删除不影响其他图书的编号顺序；
     * 且 nextId 只在 add 时自增、删除时不回收，因此删除后再新增图书，
     * 编号从历史最大值继续累加，不会顶替已删除图书的位置。
     */
    public String delete(int id) {
        Book book = findById(id);
        if (book == null) {
            return "图书编号不存在";
        }
        books.remove(book);
        return "删除成功";
    }
}
