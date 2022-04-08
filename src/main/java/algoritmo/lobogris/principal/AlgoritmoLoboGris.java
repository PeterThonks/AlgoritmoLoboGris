package algoritmo.lobogris.principal;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserDefaultVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserTreeConstants;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.List;

public class AlgoritmoLoboGris {
    public static void main(String[] args) {
        String selectSQL = "SELECT Categories.CategoryName, \n" +
                "       Products.ProductName, \n" +
                "       Sum((`Order Details`.UnitPrice*Quantity*(1-Discount)/100)*100) AS ProductSales\n" +
                "FROM Categories C\n" +
                "  INNER JOIN Products On Categories.CategoryID = Products.CategoryID\n" +
                "  INNER JOIN `Order Details` on Products.ProductID = `Order Details`.ProductID     \n" +
                "  INNER JOIN `Orders` on Orders.OrderID = `Order Details`.OrderID \n" +
                "WHERE Orders.ShippedDate Between '1997-01-01' And '1997-12-31'\n" +
                "GROUP BY Categories.CategoryName, Products.ProductName;";

        try {
            Statement select = (Statement) CCJSqlParserUtil.parse(selectSQL);
            FromItem item = ((PlainSelect) ((Select) select).getSelectBody()).get();
            System.out.println(item);

        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }
}
