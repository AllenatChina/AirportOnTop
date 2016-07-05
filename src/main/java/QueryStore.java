import java.util.ArrayList;
import java.util.List;

/**
 * Created by yin on 04/07/16.
 */
@SuppressWarnings("ALL")
public class QueryStore {

    public static Query getAllAirports() {
        return new Query().select("*").orderby("?n")
                .where("?a", "a", ":Airport")
                .where("?a", ":airportName", "?n");
    }

    public static Query getAllAirlines() {
        return new Query().select("?n").orderby("?n")
                .where("?a", "a", ":Airline")
                .where("?a", ":airlineName", "?n");
    }

    public static Query getAllAirportsWithCode(String code) {
        return new Query().select("?n").orderby("?n")
                .where("?a", "a", ":Airport")
                .where("?a", ":airportName", "?n")
                .where("?a", ":airportCode", "?c")
                .where("?c", ":codeCode", "\"" + code.toUpperCase() + "\"^^xsd:string");
    }

    public static Query getAllAirlinesWithCode(String code) {
        return new Query().select("?n").orderby("?n")
                .where("?a", "a", ":Airline")
                .where("?a", ":airlineName", "?n")
                .where("?a", ":airlineCode", "?c")
                .where("?c", ":codeCode", "\"" + code.toUpperCase() + "\"^^xsd:string");
    }





    public static class Query {

        static final String PREFIX = "PREFIX : <http://www.omg.org/AirportOrz#>";

        private String select = "";

        private String where = "";

        private String orderby = "";

        List<String[]> result;
        String[] columns;

        public Query() {
            result = new ArrayList<String[]>();
        }

        public String getSparqlQuery() {

            StringBuffer buffer = new StringBuffer(PREFIX);

            buffer.append("\n");
            buffer.append("SELECT " + select);
            buffer.append("\n");
            buffer.append("WHERE {\n");
            buffer.append(where);
            buffer.append("}\n");

            if (!orderby.isEmpty()) {
                buffer.append("ORDER BY " + orderby);
                buffer.append("\n");
            }

            return buffer.toString();
        }


        public Query select(String s) {
            select += s + " ";
            return this;
        }

        public Query where(String pre, String mid, String pos) {
            where += pre + " " + mid + " " + pos + " .\n";
            return this;
        }

        public Query orderby(String s) {
            orderby += s + " ";
            return this;
        }

        public boolean hasResult() {
            return !result.isEmpty();
        }

        public String[] getColumns() {
            return columns;
        }

        public void setColumns(String[] columns) {
            this.columns = columns;
        }

        public void addRow(String[] row) {
            result.add(row);
        }

        public List<String[]> getResult() {
            return result;
        }

    }

}

