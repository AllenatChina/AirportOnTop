import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yin on 04/07/16.
 */
@SuppressWarnings("ALL")
public class QueryStore {

    public static Query getAllAirportNames() {
        return new Query().select("?n").orderby("?n")
                .where("?a", "a", ":Airport")
                .where("?a", ":airportName", "?n");
    }

    public static Query getAllAirlineNames() {
        return new Query().select("?n").orderby("?n")
                .where("?a", "a", ":Airline")
                .where("?a", ":airlineName", "?n");
    }

    public static Query getAllAirportNamesWithCode(String code) {
        return new Query().select("?n").orderby("?n")
                .where("?a", "a", ":Airport")
                .where("?a", ":airportName", "?n")
                .where("?a", ":airportCode", "?c")
                .where("?c", ":codeCode", "\"" + code.toUpperCase() + "\"^^xsd:string");
    }



    public static class Query {

        static final String PREFIX = "PREFIX : <http://www.omg.org/AirportOrz#>";

        private String select = "";

        private String where = "";

        private String orderby = "";

        Map<String, List<String>> result;

        public Query() {
            result = new HashMap<String, List<String>>();
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
            if (!result.isEmpty()) {
                for (String s : result.keySet()) {
                    if (!result.get(s).isEmpty()) {
                        return true;
                    }
                }
            }
            return false;
        }

        public Map<String, List<String>> getResult() {

            return result;

        }

    }

}

