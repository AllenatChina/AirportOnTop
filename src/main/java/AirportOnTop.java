import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("ALL")
public class AirportOnTop extends JDialog {
    private JPanel contentPane;
    private JTextField apName;
    private JTextField apCode;
    private JTextField apCity;
    private JTextField apCountry;
    private JTextField alName;
    private JTextField alCode;
    private JTextField rtFrom;
    private JTextField alAlias;
    private JTextField alCallsign;
    private JTextField apRegion;
    private JButton apQuery;
    private JButton alQuery;
    private JRadioButton rtFromCode;
    private JRadioButton rtFromCountry;
    private JRadioButton rtFromCity;
    private JTextField rtTo;
    private JRadioButton rtToCode;
    private JRadioButton rtToCity;
    private JRadioButton rtToCountry;
    private JButton rtQuery;
    private JLabel hint;
    private JTextField rtAirline;

    static String owlFile = "src/main/resources/ontology/AirportProtege.owl";
    static String obdaFile = "src/main/resources/ontology/AirportProtege.obda";

    private OntopConnector connector;

    public AirportOnTop() {
        setContentPane(contentPane);
        setModal(true);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ButtonGroup groupFrom = new ButtonGroup();
        groupFrom.add(rtFromCountry);groupFrom.add(rtFromCode);groupFrom.add(rtFromCity);

        ButtonGroup groupTo = new ButtonGroup();
        groupTo.add(rtToCity);groupTo.add(rtToCode);groupTo.add(rtToCountry);


        apQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QueryStore.Query query = new QueryStore.Query()
                        .select("?ontology").orderby("?ontology")
                        .where("?a", "a", ":Airport").where("?a",":airportName","?ontology");

                if (!apName.getText().isEmpty()) {
                    query.where("?a", ":airportName", "\"" + apName.getText() + "\"^^xsd:string");
                }
                query.where("?a", ":airportCode", "?c").where("?c", ":codeCode", "?code").select("?code");
                if (!apCode.getText().isEmpty()) {
                    query.where("?c", ":codeCode", "\"" + apCode.getText() + "\"^^xsd:string");
                }
                query.where("?a", ":airportCity", "?ci").where("?ci", ":cityName", "?city").select("?city");
                if (!apCity.getText().isEmpty()) {
                    query.where("?ci", ":cityName", "\"" + apCity.getText() + "\"^^xsd:string");
                }
                query.where("?a", ":airportCity", "?ci")
                        .where("?ci", ":from", "?co").where("?co", ":countryName", "?country").select("?country");
                if (!apCountry.getText().isEmpty()) {
                    query.where("?co", ":countryName", "\"" + apCountry.getText() + "\"^^xsd:string");
                }
                query.where("?a", ":airportCity", "?ci")
                        .where("?ci", ":from", "?co").where("?co", ":locatedIn", "?re")
                        .where("?re", ":regionName", "?region").select("?region");
                if (!apRegion.getText().isEmpty()) {
                    query.where("?re", ":regionName", "\"" + apRegion.getText() + "\"^^xsd:string");
                }

                executeQuery(query);
            }
        });

        alQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QueryStore.Query query = new QueryStore.Query()
                        .select("?airline").select("?code").select("?alias").select("?callsign").orderby("?airline")
                        .where("?a", "a", ":Airline").where("?a", ":airlineName", "?airline")
                        .where("?a", ":airlineCode", "?c").where("?c", ":codeCode", "?code")
                        .where("?a", ":airlineCallSign", "?callsign").where("?a", ":airlineAlias", "?alias");
                if (!alName.getText().isEmpty()) {
                    query.where("?a", ":airlineName", "\"" + alName.getText() + "\"^^xsd:string");
                }
                if (!alCode.getText().isEmpty()) {
                    query.where("?c", ":codeCode", "\"" + alCode.getText() + "\"^^xsd:string");
                }
                if (!alAlias.getText().isEmpty()) {
                    query.where("?a", ":airlineAlias", "\"" + alAlias.getText() + "\"^^xsd:string");
                }
                if (!alCallsign.getText().isEmpty()) {
                    query.where("?a", ":airlineCallsign", "\"" + alCallsign.getText() + "\"^^xsd:string");
                }

                executeQuery(query);
            }
        });

        rtQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QueryStore.Query query = new QueryStore.Query()
                        .select("?airline").select("?fromCity").select("?fromAirport")
                        .select("?toCity").select("?toAirport").select("?stops")
                        .where("?r", "a", ":Route").where("?r", ":routeStops", "?stops")
                        .where("?r", ":belongsTo", "?ai").where("?ai", ":airlineName", "?airline")
                        .where("?r", ":departsFrom", "?d").where("?d", ":airportName", "?fromAirport")
                        .where("?d", ":airportCity", "?ci1").where("?ci1", ":cityName", "?fromCity")
                        .where("?r", ":arrivesAt", "?a").where("?a", ":airportName", "?toAirport")
                        .where("?a", ":airportCity", "?ci2").where("?ci2", ":cityName", "?toCity");

                if (!rtFrom.getText().isEmpty()) {
                    if (rtFromCode.isSelected()) {
                        query.where("?d", ":airportCode", "?c1").where("?c1", ":codeCode", "\"" + rtFrom.getText() + "\"^^xsd:string");
                    } else if (rtFromCity.isSelected()) {
                        query.where("?d", ":airportCity", "?ci1").where("?ci1", ":cityName", "\"" + rtFrom.getText() + "\"^^xsd:string");
                    } else if (rtFromCountry.isSelected()) {
                        query.where("?d", ":airportCity", "?ci1")
                                .where("?ci1", ":from", "?co1").where("?co1", ":countryName", "\"" + rtFrom.getText() + "\"^^xsd:string");
                    }
                }

                if (!rtTo.getText().isEmpty()) {
                    if (rtToCode.isSelected()) {
                        query.where("?a", ":airportCode", "?c2").where("?c2", ":codeCode", "\"" + rtTo.getText() + "\"^^xsd:string");
                    } else if (rtToCity.isSelected()) {
                        query.where("?a", ":airportCity", "?ci2").where("?ci2", ":cityName", "\"" + rtTo.getText() + "\"^^xsd:string");
                    } else if (rtToCountry.isSelected()) {
                        query.where("?a", ":airportCity", "?ci2")
                                .where("?ci2", ":from", "?co2").where("?co2", ":countryName", "\"" + rtTo.getText() + "\"^^xsd:string");
                    }
                }

                if (!rtAirline.getText().isEmpty()) {
                    query.where("?ai", ":airlineName", "\"" + rtAirline.getText() + "\"^^xsd:string");
                }

                executeQuery(query);
            }
        });

        connector = OntopConnector.createOntopConnector(owlFile, obdaFile);
    }

    private void executeQuery(QueryStore.Query q) {
        try {
            hint.setText("Retrieving data...");

            connector.executeQuery(q);

            if (q.hasResult()) {
                hint.setText("Welcome to AirportOnTop!");
                Result resultGUI = new Result();
                resultGUI.pack();
                resultGUI.setTable(q.getResult(), q.getColumns());
                resultGUI.setVisible(true);
            } else {
                hint.setText("No results of this query were found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            hint.setText("Error!");
        }
    }

    private void onCancel() {
        if (connector != null) {
            connector.close();
        }
        dispose();
    }

    public static void main(String[] args) {
        AirportOnTop dialog = new AirportOnTop();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
