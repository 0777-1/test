import java.util.List;

/**
 * T2 删除功能实现后的测试：删除成功/失败提示、书库移除、编号不复用、删空后编号延续。
 *
 * 约定：类名以 Test 结尾；测试方法为无参 public void 且以 test 开头；使用 Assert 断言。
 * 直接 new Library(true) 调用，不依赖控制台。
 */
public class DeleteTest {

    /** 删除存在的图书返回「删除成功」。 */
    public void testDeleteExistingReturnsSuccess() {
        Library library = new Library(true);
        Assert.assertEquals("删除成功", library.delete(1), "删除编号 1 应成功");
        Assert.assertEquals(2, library.listAll().size(), "删除后应剩 2 本");
        Assert.assertNull(library.findById(1), "删除后按编号 1 应查不到");
    }

    /** 删除后 listAll() 中不再包含该书。 */
    public void testDeleteRemovesFromListAll() {
        Library library = new Library(true);
        library.delete(2);
        List<Book> books = library.listAll();
        Assert.assertEquals(2, books.size(), "删除后应剩 2 本");
        for (Book book : books) {
            Assert.assertTrue(book.getId() != 2, "列表不应再包含编号 2 的图书");
        }
        Assert.assertEquals(1, books.get(0).getId(), "剩余第一本应为编号 1");
        Assert.assertEquals(3, books.get(1).getId(), "剩余第二本应为编号 3");
    }

    /** 删除不回收编号：删掉编号 2 之后再添加，新书得编号 4 而不是 2。 */
    public void testIdNotReusedAfterDelete() {
        Library library = new Library(true);
        Assert.assertEquals("删除成功", library.delete(2), "删除编号 2 应成功");
        Book added = library.add("编译原理", "孙八", 75.0);
        Assert.assertEquals(4, added.getId(), "删除后再添加应得编号 4，不复用已删除的 2");
        Assert.assertNull(library.findById(2), "编号 2 仍应查不到（未被新书顶替）");
        Assert.assertEquals(3, library.listAll().size(), "删 1 本添 1 本后应剩 3 本");
    }

    /** 重复删除同一编号：第二次应返回「图书编号不存在」。 */
    public void testDeleteTwiceReportsMissing() {
        Library library = new Library(true);
        Assert.assertEquals("删除成功", library.delete(3), "首次删除应成功");
        Assert.assertEquals("图书编号不存在", library.delete(3), "重复删除应提示编号不存在");
        Assert.assertEquals(2, library.listAll().size(), "重复删除不应改变剩余数量");
    }

    /** 编号 0、负数、超出范围：均返回「图书编号不存在」。 */
    public void testDeleteZeroAndNegativeId() {
        Library library = new Library(true);
        Assert.assertEquals("图书编号不存在", library.delete(0), "编号 0 不存在");
        Assert.assertEquals("图书编号不存在", library.delete(-1), "负编号不存在");
        Assert.assertEquals("图书编号不存在", library.delete(-99), "负编号不存在");
        Assert.assertEquals("图书编号不存在", library.delete(999), "超范围编号不存在");
        Assert.assertEquals(3, library.listAll().size(), "失败的删除不应改变书库");
    }

    /** 删空之后继续添加：编号从历史最大值继续延续，不回到 1。 */
    public void testIdContinuesAfterDeleteAll() {
        Library library = new Library(true);
        Assert.assertEquals("删除成功", library.delete(1), "删除编号 1");
        Assert.assertEquals("删除成功", library.delete(2), "删除编号 2");
        Assert.assertEquals("删除成功", library.delete(3), "删除编号 3");
        Assert.assertEquals(0, library.listAll().size(), "全部删除后应为空库");
        Assert.assertEquals("图书编号不存在", library.delete(1), "空库删除应提示不存在");
        Book again = library.add("重新上架", "周九", 30.0);
        Assert.assertEquals(4, again.getId(), "删空后新增应延续为编号 4");
        Assert.assertEquals(1, library.listAll().size(), "空库添加后应有 1 本");
    }

    /** 删掉中间的图书后，其余图书编号顺序不变。 */
    public void testRemainingIdsNotShifted() {
        Library library = new Library(true);
        library.delete(1);
        List<Book> books = library.listAll();
        Assert.assertEquals(2, books.size(), "删除后应剩 2 本");
        Assert.assertEquals(2, books.get(0).getId(), "剩余首本编号应仍为 2");
        Assert.assertEquals(3, books.get(1).getId(), "剩余次本编号应仍为 3");
        Assert.assertEquals("数据结构", books.get(0).getName(), "剩余首本内容不应错位");
    }
}
