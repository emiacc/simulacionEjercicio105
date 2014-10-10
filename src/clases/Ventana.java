package clases;

import static clases.Aleatorios.*;
import java.awt.Component;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Emiliano
 */
public class Ventana extends javax.swing.JFrame {

    static final Recorrido uno = new Recorrido(1, new int[]{1, 3, 4});
    static final Recorrido dos = new Recorrido(2, new int[]{1, 2, 3, 4});
    static final Recorrido tres = new Recorrido(3, new int[]{1, 4});

    DefaultTableModel modelo0;
    int recorrido1, recorrido2, recorrido3, capacidadA, capacidadB, capacidadC, capacidadD, col, nroFila, colaMaxA, id;
    double aInf, aSup, bInf, bSup, cMed, cDs, dMed, dDs, lledadaMed, loteMed, desde, hasta, horaActual, tiempoLlegada, proximaLlegada,
            visitaA, visitaB, visitaC, visitaD, salidaA, salidaB, salidaC, salidaD;
    int i, tamFila, lote, personas, terminadas;
    Recorrido reco;
    String eventoActual, tSalaA, tSalaB, tSalaC, tSalaD;
    Persona p;
    Object[] vector;
    SimpleList salaA, salaB, salaC, salaD;

    Queue<Persona> colaA;
    Queue<Persona> colaB;
    Queue<Persona> colaC;
    Queue<Persona> colaD;

    public Ventana() {
        initComponents();

        setLocationRelativeTo(null); // Centrar la Ventana Principal
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        crearTabla();
    }

    public void simular() {
        if (!asignarParametros()) {
            valoresPorDefecto();
            return;
        }
        crearTabla();
        salaA = new SimpleList(capacidadA);
        salaB = new SimpleList(capacidadB);
        salaC = new SimpleList(capacidadC);
        salaD = new SimpleList(capacidadD);
        colaA = new LinkedList<Persona>();
        colaB = new LinkedList<Persona>();
        colaC = new LinkedList<Persona>();
        colaD = new LinkedList<Persona>();
        nroFila = 0;
        horaActual = 0.0;
        colaMaxA = 0;
        id = 0;
        tSalaA = tSalaB = tSalaC = tSalaD = "";
        personas = terminadas = 0;
        eventoActual = "Inicio";
        generarTabla();
    }

    public void generarTabla() {
        vector = new Object[tamFila];
        col = 0;
        vector[col] = nroFila;
        vector[++col] = horaActual;
        vector[++col] = eventoActual;
        horaActual = exponencial(lledadaMed);
        vector[++col] = redondear(horaActual);
        vector[++col] = redondear(horaActual);
        if(desde < 1)
          modelo0.addRow(vector);

        vector = new Object[tamFila];
        col = 0;
        vector[col] = nroFila;
        vector[++col] = redondear(horaActual);
        eventoActual = "Llegada";
        vector[++col] = eventoActual;
        eventoLlegada();

        while (horaActual < hasta) {
            simularFila();
        }
    }

    public void simularFila() {
        salidaA = salidaB = salidaC = salidaD = 0;
        if (!salaA.isEmpty()) {
            p = (Persona) salaA.getLast();
            salidaA = p.getTiempoSalida();
        }
        if (!salaB.isEmpty()) {
            p = (Persona) salaB.getLast();
            salidaB = p.getTiempoSalida();
        }
        if (!salaC.isEmpty()) {
            p = (Persona) salaC.getLast();
            salidaC = p.getTiempoSalida();
        }
        if (!salaD.isEmpty()) {
            p = (Persona) salaD.getLast();
            salidaD = p.getTiempoSalida();
        }
        double arreglo[] = new double[]{proximaLlegada, salidaA, salidaB, salidaC, salidaD};
        double menor = arreglo[0];
        int menorEvento = 0;
        for (int i = 1; i < arreglo.length; i++) {
            if (arreglo[i] < menor && arreglo[i] != 0) {
                menorEvento = i;
                menor = arreglo[i];
            }
        }
        horaActual = menor;
        vector = new Object[tamFila];
        col = 0;
        vector[col] = ++nroFila;
        vector[++col] = redondear(horaActual);
        switch (menorEvento) {
            case 0:
                eventoActual = "Llegada";
                vector[++col] = eventoActual;
                eventoLlegada();
                break;
            case 1:
                eventoActual = "Fin V. A";
                salidaA();
                break;
            case 2:
                eventoActual = "Fin V. B";
                salidaB();
                break;
            case 3:
                eventoActual = "Fin V. C";
                salidaC();
                break;
            case 4:
                eventoActual = "Fin Visita";
                salidaD();
                break;
        }
       // tblTabla0.changeSelection(tblTabla0.getRowCount() - 1, 0, false, false);
    }

    public void mostrarColas() {
        col = 10;
        vector[col] = salaA.size();
        vector[++col] = colaA.size();
        vector[++col] = colaMaxA;
        col += 3;
        vector[col] = salaB.size();
        vector[++col] = colaB.size();
        col += 3;
        vector[col] = salaC.size();
        vector[++col] = colaC.size();
        col += 3;
        vector[col] = salaD.size();
        vector[++col] = colaD.size();
        vector[++col] = salaA.toString();
        vector[++col] = salaB.toString();
        vector[++col] = salaC.toString();
        vector[++col] = salaD.toString();
        vector[++col] = colaA.toString();
        vector[++col] = colaB.toString();
        vector[++col] = colaC.toString();
        vector[++col] = colaD.toString();
        
        if(horaActual > desde)
            modelo0.addRow(vector);
    }

    public void salidaA() {

        p = (Persona) salaA.removeLast();
        vector[++col] = eventoActual + " #" + p.getId();
        col+=2;
        vector[col] = redondear(proximaLlegada);
        switch (p.getProxSala()) {
            case 2:
                salaB();
                break;
            case 3:
                salaC();
                break;
            case 4:
                salaD();
                break;
        }
        if (!colaA.isEmpty()) {
            p = colaA.remove();
            p.setEstado(0);
            visitaA = uniforme(aInf, aSup);
            p.setTiempoSalida(horaActual + visitaA);
            col = 6;
            vector[col] = "#" + p.getId();
            vector[++col] = p.getRecorrido();
            vector[++col] = redondear(visitaA);
            vector[++col] = redondear(visitaA + horaActual);
            salaA.addInOrder(p);
        }
        mostrarColas();

    }

    public void salidaB() {

        p = (Persona) salaB.removeLast();
        vector[++col] = eventoActual + " #" + p.getId();
        col+=2;
        vector[col] = redondear(proximaLlegada);
        switch (p.getProxSala()) {
            case 3:
                salaC();
                break;
            case 4:
                salaD();
                break;
        }
        if (!colaB.isEmpty()) {
            p = colaB.remove();
            p.setEstado(0);
            visitaB = uniforme(bInf, bSup);
            p.setTiempoSalida(horaActual + visitaB);
            col = 13;
            vector[col] = redondear(visitaB);
            vector[++col] = redondear(visitaB + horaActual);
            salaB.addInOrder(p);
        }
        mostrarColas();
    }

    public void salidaC() {
        p = (Persona) salaC.removeLast();
        vector[++col] = eventoActual + " #" + p.getId();
        col+=2;
        vector[col] = redondear(proximaLlegada);
        switch (p.getProxSala()) {
            case 4:
                salaD();
                break;
        }
        if (!colaC.isEmpty()) {
            p = colaC.remove();
            p.setEstado(0);
            visitaC = normal(cMed, cDs);
            p.setTiempoSalida(horaActual + visitaC);
            col = 17;
            vector[col] = redondear(visitaC);
            vector[++col] = redondear(visitaC + horaActual);
            salaC.addInOrder(p);
        }
        mostrarColas();
    }

    public void salidaD() {
        terminadas++;
        p = (Persona) salaD.removeLast();
        vector[++col] = eventoActual + " #" + p.getId();
        col+=2;
        vector[col] = redondear(proximaLlegada);
        p.setSalaActual(0);
        p.setEstado(2);
        if (!colaD.isEmpty()) {
            p = colaD.remove();
            p.setEstado(0);
            visitaD = normal(dMed, dDs);
            p.setTiempoSalida(horaActual + visitaD);
            col = 21;
            vector[col] = redondear(visitaD);
            vector[++col] = redondear(visitaD + horaActual);
            salaD.addInOrder(p);
        }
        mostrarColas();
    }

    public void salaB() {
        col = 13;
        if (salaB.size() < capacidadB) {
            visitaB = uniforme(bInf, bSup);
            vector[col] = redondear(visitaB);
            vector[++col] = redondear(visitaB + horaActual);
            p.setSalaActual(2);
            p.setTiempoSalida(visitaB + horaActual);
            p.setEstado(0);
            salaB.addInOrder(p);
        } else {
            p.setEstado(1);
            p.setSalaActual(2);
            colaB.add(p);
            col++;
        }
    }

    public void salaC() {
        col = 17;
        if (salaC.size() < capacidadC) {
            visitaC = normal(cMed, cDs);
            vector[col] = redondear(visitaC);
            vector[++col] = redondear(visitaC + horaActual);
            p.setSalaActual(3);
            p.setTiempoSalida(visitaC + horaActual);
            p.setEstado(0);
            salaC.addInOrder(p);
        } else {
            p.setEstado(1);
            p.setSalaActual(3);
            colaC.add(p);
            col++;
        }

    }

    public void salaD() {
        col = 21;
        if (salaD.size() < capacidadD) {
            visitaD = normal(dMed, dDs);
            vector[col] = redondear(visitaD);
            vector[++col] = redondear(visitaD + horaActual);
            p.setSalaActual(4);
            p.setTiempoSalida(visitaD + horaActual);
            p.setEstado(0);
            salaD.addInOrder(p);
        } else {
            p.setEstado(1);
            p.setSalaActual(4);
            colaD.add(p);
            col++;
        }
    }

    public void eventoLlegada() {

        tiempoLlegada = exponencial(lledadaMed);
        proximaLlegada = horaActual + tiempoLlegada;
        lote = poisson(loteMed);

        vector[++col] = redondear(tiempoLlegada);
        vector[++col] = redondear(proximaLlegada);
        vector[++col] = lote;
        if(horaActual > desde)
            modelo0.addRow(vector);

        while (lote != 0) {
            personas++;
            //vector = new Object[tamFila+(personas*2)];
            vector = new Object[tamFila];
            col = 6;
            id++;
            vector[col] = "#" + id;
            reco = obtenerRecorrdio();
            vector[++col] = reco.toString();
            if (salaA.size() < capacidadA) {
                visitaA = uniforme(aInf, aSup);
                p = new Persona(reco, visitaA + horaActual, id);
                p.setEstado(0);
                salaA.addInOrder(p);
                vector[++col] = redondear(visitaA);
                vector[++col] = redondear(visitaA + horaActual);
                vector[++col] = salaA.size();
                vector[++col] = colaA.size();
                vector[++col] = colaMaxA;
            } else {
                p = new Persona(reco, id);
                p.setEstado(1);
                colaA.add(p);
                if (colaA.size() > colaMaxA) {
                    colaMaxA = colaA.size();
                }
                col += 2;
                vector[++col] = salaA.size();
                vector[++col] = colaA.size();
                vector[++col] = colaMaxA;
            }
            col += 3;
            vector[col] = salaB.size();
            vector[++col] = colaB.size();
            col += 3;
            vector[col] = salaC.size();
            vector[++col] = colaC.size();
            col += 3;
            vector[col] = salaD.size();
            vector[++col] = colaD.size();
            lote--;
            vector[++col] = salaA.toString();
            vector[++col] = salaB.toString();
            vector[++col] = salaC.toString();
            vector[++col] = salaD.toString();
            vector[++col] = colaA.toString();
            vector[++col] = colaB.toString();
            vector[++col] = colaC.toString();
            vector[++col] = colaD.toString();
            if(horaActual > desde)    
                modelo0.addRow(vector);

        }
        //organizarTabla();
    }

    public void valoresPorDefecto() {
        txtRecoorido1.setText("40");
        txtRecoorido2.setText("20");
        txtRecoorido3.setText("40");
        txtCapacidadA.setText("80");
        txtCapacidadB.setText("40");
        txtCapacidadC.setText("40");
        txtCapacidadD.setText("70");
        txtAInf.setText("21");
        txtASup.setText("39");
        txtBInf.setText("13");
        txtBSup.setText("37");
        txtCMed.setText("11");
        txtCDS.setText("3");
        txtDMed.setText("6");
        txtDDS.setText("2");
        txtLlegadaMed.setText("5");
        txtPoissonMed.setText("4");
        txtDesde.setText("0");
        txtHasta.setText("100");
    }

    public boolean asignarParametros() {
        try {
            recorrido1 = Integer.parseInt(txtRecoorido1.getText());
            recorrido2 = Integer.parseInt(txtRecoorido2.getText());
            recorrido3 = Integer.parseInt(txtRecoorido3.getText());
            capacidadA = Integer.parseInt(txtCapacidadA.getText());
            capacidadB = Integer.parseInt(txtCapacidadB.getText());
            capacidadC = Integer.parseInt(txtCapacidadC.getText());
            capacidadD = Integer.parseInt(txtCapacidadD.getText());
            aInf = Double.parseDouble(txtAInf.getText());
            aSup = Double.parseDouble(txtASup.getText());
            bInf = Double.parseDouble(txtBInf.getText());
            bSup = Double.parseDouble(txtBSup.getText());
            cMed = Double.parseDouble(txtCMed.getText());
            cDs = Double.parseDouble(txtCDS.getText());
            dMed = Double.parseDouble(txtDMed.getText());
            dDs = Double.parseDouble(txtDDS.getText());
            lledadaMed = Double.parseDouble(txtLlegadaMed.getText());
            loteMed = Double.parseDouble(txtPoissonMed.getText());
            desde = Double.parseDouble(txtDesde.getText());
            hasta = Double.parseDouble(txtHasta.getText());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, " ¡Datos incorrectos! Se asignan los valores por defecto. ", " Atención ", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (recorrido1 + recorrido2 + recorrido3 != 100) {
            JOptionPane.showMessageDialog(null, " La suma de los porcentajes de los recorridos debe ser 100. ", " Atención ", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (aInf >= aSup || bInf >= bSup || desde >= hasta) {
            JOptionPane.showMessageDialog(null, " Límite inferior debe ser menor a límite superior. ", " Atención ", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void crearTabla() {
        modelo0 = new DefaultTableModel() {
            @Override // Evita que se modifique el contenido de la Tabla
            public boolean isCellEditable(int fila, int columna) {
                if (columna >= 0) {
                    return false;
                } else {
                    return super.isCellEditable(fila, columna);
                }
            }
        };
        modelo0.addColumn("Nº");
        modelo0.addColumn("Reloj");
        modelo0.addColumn("Evento");

        modelo0.addColumn("T LLegada");
        modelo0.addColumn("Prox Lleg");

        modelo0.addColumn("Cantidad");
        modelo0.addColumn("ID");
        modelo0.addColumn("Recorrido");

        modelo0.addColumn("T visita A");
        modelo0.addColumn("T fin V A");
        modelo0.addColumn("Personas");
        modelo0.addColumn("Cola A");
        modelo0.addColumn("Cola Max A");

        modelo0.addColumn("T visita B");
        modelo0.addColumn("T fin V B");
        modelo0.addColumn("Personas");
        modelo0.addColumn("Cola B");

        modelo0.addColumn("T visita C");
        modelo0.addColumn("T fin V C");
        modelo0.addColumn("Personas");
        modelo0.addColumn("Cola C");

        modelo0.addColumn("T visita D");
        modelo0.addColumn("T fin V D");
        modelo0.addColumn("Personas");
        modelo0.addColumn("Cola D");

        modelo0.addColumn("Tiempos A");
        modelo0.addColumn("Tiempos B");
        modelo0.addColumn("Tiempos C");
        modelo0.addColumn("Tiempos D");
        modelo0.addColumn("Cola A");
        modelo0.addColumn("Cola B");
        modelo0.addColumn("Cola C");
        modelo0.addColumn("Cola D");
        tblTabla0.setModel(modelo0);

        organizarTabla();

    }

    public void organizarTabla() {
        tamFila = tblTabla0.getColumnCount();

        tblTabla0.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblTabla0.getColumnModel().getColumn(0).setResizable(false);

        for (i = 1; i < tamFila; i++) {
            tblTabla0.getColumnModel().getColumn(i).setPreferredWidth(65);

        }
        tblTabla0.getColumnModel().getColumn(2).setPreferredWidth(85);
        tblTabla0.getColumnModel().getColumn(6).setPreferredWidth(50);

        centrarValores(tblTabla0, tamFila);
        tblTabla0.getColumnModel().getColumn(0).setCellRenderer(tblTabla0.getTableHeader().getDefaultRenderer());
        tblTabla0.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    }

    public Recorrido obtenerRecorrdio() {
        float limite1 = recorrido1 - 1;
        float limite2 = recorrido2;
        limite1 = (limite1 / 100);
        limite2 = (limite2 / 100) + limite1;
        double nro = random();
        if (nro < limite1) {
            return uno;
        } else if (nro < limite2) {
            return dos;
        } else {
            return tres;
        }
    }

    public void centrarValores(JTable tabla, int columnas) { // Creo y asigno a las columnas de la Tabla un formato con alineacion centrada
        TableCellRenderer render = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                return l;
            }
        };
        for (i = 0; i < columnas; i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(render);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtRecoorido1 = new javax.swing.JTextField();
        txtRecoorido2 = new javax.swing.JTextField();
        txtRecoorido3 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCapacidadA = new javax.swing.JTextField();
        txtCapacidadB = new javax.swing.JTextField();
        txtCapacidadC = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCapacidadD = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtAInf = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtASup = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtBInf = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtBSup = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtCMed = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtDMed = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtCDS = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtDDS = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtLlegadaMed = new javax.swing.JTextField();
        txtPoissonMed = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtDesde = new javax.swing.JTextField();
        txtHasta = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabelPersonas = new javax.swing.JLabel();
        jLabelSalieron = new javax.swing.JLabel();
        jLabelColaMax = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTabla0 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Configuración de Parametros");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Recorrido %"));

        jLabel2.setText("Recorrido 1:");

        jLabel3.setText("Recorrido 2:");

        jLabel4.setText("Recorrido 3:");

        txtRecoorido1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRecoorido1.setText("40");
        txtRecoorido1.setToolTipText("A - C - D");

        txtRecoorido2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRecoorido2.setText("20");
        txtRecoorido2.setToolTipText("A - B - C - D");

        txtRecoorido3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRecoorido3.setText("40");
        txtRecoorido3.setToolTipText("A - D");
        txtRecoorido3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRecoorido3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRecoorido3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRecoorido1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRecoorido2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtRecoorido1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtRecoorido2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtRecoorido3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Salas"));

        jLabel6.setText("Capacidad A:");

        jLabel7.setText("Capacidad B:");

        jLabel8.setText("Capacidad C:");

        txtCapacidadA.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCapacidadA.setText("80");
        txtCapacidadA.setToolTipText("");

        txtCapacidadB.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCapacidadB.setText("40");
        txtCapacidadB.setToolTipText("");

        txtCapacidadC.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCapacidadC.setText("40");
        txtCapacidadC.setToolTipText("");
        txtCapacidadC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCapacidadCActionPerformed(evt);
            }
        });

        jLabel10.setText("Capacidad D:");

        txtCapacidadD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCapacidadD.setText("70");
        txtCapacidadD.setToolTipText("");
        txtCapacidadD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCapacidadDActionPerformed(evt);
            }
        });

        jLabel11.setText("Uniforme Inf: ");

        txtAInf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAInf.setText("21");
        txtAInf.setToolTipText("");
        txtAInf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAInfActionPerformed(evt);
            }
        });

        jLabel12.setText("Sup:");

        txtASup.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtASup.setText("39");
        txtASup.setToolTipText("");
        txtASup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtASupActionPerformed(evt);
            }
        });

        jLabel13.setText("Uniforme Inf: ");

        txtBInf.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBInf.setText("13");
        txtBInf.setToolTipText("");

        jLabel14.setText("Sup:");

        txtBSup.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBSup.setText("37");
        txtBSup.setToolTipText("");
        txtBSup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBSupActionPerformed(evt);
            }
        });

        jLabel15.setText("Normal Media");

        txtCMed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCMed.setText("11");
        txtCMed.setToolTipText("");
        txtCMed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCMedActionPerformed(evt);
            }
        });

        jLabel16.setText("Normal Media");

        txtDMed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDMed.setText("6");
        txtDMed.setToolTipText("");
        txtDMed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDMedActionPerformed(evt);
            }
        });

        jLabel17.setText("  Ds:");

        txtCDS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCDS.setText("3");
        txtCDS.setToolTipText("");
        txtCDS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCDSActionPerformed(evt);
            }
        });

        jLabel18.setText("  Ds:");

        txtDDS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDDS.setText("2");
        txtDDS.setToolTipText("");
        txtDDS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDDSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCapacidadC, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCapacidadD, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCapacidadB, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCapacidadA, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel11)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAInf, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBInf, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCMed, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDMed, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtASup, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBSup, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCDS, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDDS, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCapacidadA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtAInf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtASup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtCapacidadB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtBInf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtBSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCapacidadC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(txtCMed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(txtCDS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtCapacidadD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(txtDMed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(txtDDS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Llegada"));

        jLabel5.setText("Tiempo entre llegada exponencial media:");

        jLabel9.setText("Lote de llegada Poisson media:");

        txtLlegadaMed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtLlegadaMed.setText("5");
        txtLlegadaMed.setToolTipText("");
        txtLlegadaMed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLlegadaMedActionPerformed(evt);
            }
        });

        txtPoissonMed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPoissonMed.setText("4");
        txtPoissonMed.setToolTipText("");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtLlegadaMed, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPoissonMed, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtLlegadaMed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPoissonMed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Simular");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Mostrar"));

        jLabel20.setText("Desde minuto");

        jLabel21.setText("Hasta minuto");

        txtDesde.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDesde.setText("0");
        txtDesde.setToolTipText("");
        txtDesde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDesdeActionPerformed(evt);
            }
        });

        txtHasta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHasta.setText("100");
        txtHasta.setToolTipText("");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(txtHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 668, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Resultados");

        jLabel22.setText("Personas que ingresaron:");

        jLabel23.setText("Personas que salieron:");

        jLabel24.setText("Cola máxima en el ingreso:");

        jLabelPersonas.setText("0");

        jLabelSalieron.setText("0");

        jLabelColaMax.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPersonas, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelSalieron, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelColaMax, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabelPersonas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabelSalieron))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabelColaMax))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(null);

        tblTabla0.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTabla0.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTabla0MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTabla0);
        tblTabla0.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 38, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtRecoorido3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRecoorido3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRecoorido3ActionPerformed

    private void txtCapacidadCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCapacidadCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCapacidadCActionPerformed

    private void txtCapacidadDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCapacidadDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCapacidadDActionPerformed

    private void txtAInfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAInfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAInfActionPerformed

    private void txtASupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtASupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtASupActionPerformed

    private void txtBSupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBSupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBSupActionPerformed

    private void txtCMedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCMedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCMedActionPerformed

    private void txtDMedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDMedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDMedActionPerformed

    private void txtCDSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCDSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCDSActionPerformed

    private void txtDDSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDDSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDDSActionPerformed

    private void txtLlegadaMedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLlegadaMedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLlegadaMedActionPerformed

    private void txtDesdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDesdeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDesdeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        new Thread(new Runnable() {
            @Override
            public void run() {
                simular();
                jLabelPersonas.setText(personas + "");
                jLabelSalieron.setText(terminadas + "");
                jLabelColaMax.setText(colaMaxA + "");
            }
        }).start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tblTabla0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTabla0MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if (tblTabla0.getSelectedColumn() > 24) {
                JOptionPane.showMessageDialog(this,
                        "<html><body><p style='width: 600px;'>" + tblTabla0.getValueAt(tblTabla0.getSelectedRow(), tblTabla0.getSelectedColumn()) + "</body></html>",
                        "Sala",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_tblTabla0MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelColaMax;
    private javax.swing.JLabel jLabelPersonas;
    private javax.swing.JLabel jLabelSalieron;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblTabla0;
    private javax.swing.JTextField txtAInf;
    private javax.swing.JTextField txtASup;
    private javax.swing.JTextField txtBInf;
    private javax.swing.JTextField txtBSup;
    private javax.swing.JTextField txtCDS;
    private javax.swing.JTextField txtCMed;
    private javax.swing.JTextField txtCapacidadA;
    private javax.swing.JTextField txtCapacidadB;
    private javax.swing.JTextField txtCapacidadC;
    private javax.swing.JTextField txtCapacidadD;
    private javax.swing.JTextField txtDDS;
    private javax.swing.JTextField txtDMed;
    private javax.swing.JTextField txtDesde;
    private javax.swing.JTextField txtHasta;
    private javax.swing.JTextField txtLlegadaMed;
    private javax.swing.JTextField txtPoissonMed;
    private javax.swing.JTextField txtRecoorido1;
    private javax.swing.JTextField txtRecoorido2;
    private javax.swing.JTextField txtRecoorido3;
    // End of variables declaration//GEN-END:variables

}
