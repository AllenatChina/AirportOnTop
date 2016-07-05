import utils.Utils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.*;
import java.util.List;

public class Result extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTable table;

    public Result() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        dispose();
    }

    public void setTable(List<String[]> data, String[] cols) {
        table.setModel(new AirportTableModel(data, cols));
    }

    public class AirportTableModel extends AbstractTableModel {

        List<String[]> data;
        String[] cols;

        public AirportTableModel(List<String[]> data, String[] cols) {
            this.data = data;
            this.cols = cols;
        }

        @Override
        public String getColumnName(int column) {
            return cols[column];
        }

        public int getRowCount() {
            return data.size();
        }

        public int getColumnCount() {
            return cols.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return Utils.filterDataType(data.get(rowIndex)[columnIndex]);
        }

    }


    public static void main(String[] args) {
        Result dialog = new Result();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
