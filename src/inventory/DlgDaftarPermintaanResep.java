package inventory;
import fungsi.BackgroundMusic;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import static inventory.DlgResepObat.intToRoman;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import simrskhanza.DlgCariBangsal;
import simrskhanza.DlgCariPoli;

public class DlgDaftarPermintaanResep extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabMode2,tabMode3,tabMode4,tabMode5,tabMode6;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private PreparedStatement ps,ps2,ps3,ps4,psracikan,psracikan2;
    private ResultSet rs,rs2,rs3,rs4,rsracikan,rsracikan2;
    private DlgCariObat dlgobt=new DlgCariObat(null,false);
    private DlgCariObat2 dlgobt2=new DlgCariObat2(null,false);
    private DlgInputStokPasien dlgstok=new DlgInputStokPasien(null,false);
    private String bangsal="",aktifkanparsial="no",kamar="",alarm="",
            formalarm="",nol_detik,detik,NoResep="",TglPeresepan="",JamPeresepan="",
            NoRawat="",NoRM="",Pasien="",DokterPeresep="",Status="",KodeDokter="",Ruang="",KodeRuang="",
            namaobat="",namaobat1="",rincianobat="",latin_racikan="",finger="",tgl_lahir="",bb="",alergi="";
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private DlgCariPoli poli=new DlgCariPoli(null,false);
    private DlgCariBangsal ruang=new DlgCariBangsal(null,false);
    private int jmlparsial=0,nilai_detik,resepbaru=0,i=0;
    private BackgroundMusic music;
    private boolean aktif=false,semua;
    
    /** Creates new form 
     * @param parent
     * @param modal */
    public DlgDaftarPermintaanResep(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        tabMode=new DefaultTableModel(null,new Object[]{
                "No.Resep","Tgl.Peresepan","Jam Peresepan","No.Rawat","No.RM","Pasien","Dokter Peresep",
                "Status","Kode Dokter","Poli/Unit","Kode Poli","Jenis Bayar"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbResepRalan.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbResepRalan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbResepRalan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i <12; i++) {
            TableColumn column = tbResepRalan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(75);
            }else if(i==1){
                column.setPreferredWidth(80);
            }else if(i==2){
                column.setPreferredWidth(85);
            }else if(i==3){
                column.setPreferredWidth(105);
            }else if(i==4){
                column.setPreferredWidth(70);
            }else if(i==5){
                column.setPreferredWidth(190);
            }else if(i==6){
                column.setPreferredWidth(190);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==9){
                column.setPreferredWidth(140);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==11){
                column.setPreferredWidth(120);
            }
        }
        tbResepRalan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabMode2=new DefaultTableModel(null,new Object[]{
                "No.Resep","Tgl.Resep","Poli/Unit","Status","Pasien","Dokter Peresep"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbDetailResepRalan.setModel(tabMode2);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbDetailResepRalan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbDetailResepRalan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 6; i++) {
            TableColumn column = tbDetailResepRalan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(75);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(140);
            }else if(i==3){
                column.setPreferredWidth(100);
            }else if(i==4){
                column.setPreferredWidth(350);
            }else if(i==5){
                column.setPreferredWidth(190);
            }
        }
        tbDetailResepRalan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabMode3=new DefaultTableModel(null,new Object[]{
                "No.Resep","Tgl.Peresepan","Jam Peresepan","No.Rawat","No.RM","Pasien","Dokter Peresep",
                "Status","Kode Dokter","Ruang/Kamar","Kode Bangsal","Jenis Bayar"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbResepRanap.setModel(tabMode3);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbResepRanap.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbResepRanap.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i <12; i++) {
            TableColumn column = tbResepRanap.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(75);
            }else if(i==1){
                column.setPreferredWidth(80);
            }else if(i==2){
                column.setPreferredWidth(85);
            }else if(i==3){
                column.setPreferredWidth(105);
            }else if(i==4){
                column.setPreferredWidth(70);
            }else if(i==5){
                column.setPreferredWidth(190);
            }else if(i==6){
                column.setPreferredWidth(190);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==9){
                column.setPreferredWidth(140);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==11){
                column.setPreferredWidth(120);
            }
        }
        tbResepRanap.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabMode4=new DefaultTableModel(null,new Object[]{
                "No.Resep","Tgl.Resep","Ruang/Kamar","Status","Pasien","Dokter Peresep"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbDetailResepRanap.setModel(tabMode4);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbDetailResepRanap.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbDetailResepRanap.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 6; i++) {
            TableColumn column = tbDetailResepRanap.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(75);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(140);
            }else if(i==3){
                column.setPreferredWidth(100);
            }else if(i==4){
                column.setPreferredWidth(350);
            }else if(i==5){
                column.setPreferredWidth(190);
            }
        }
        tbDetailResepRanap.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabMode5=new DefaultTableModel(null,new Object[]{
                "No.Permintaan","Tanggal","Jam","No.Rawat","No.RM","Pasien","Dokter Yang Meminta",
                "Status","Kode Dokter","Ruang/Kamar","Kode Bangsal","Jenis Bayar"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbPermintaanStok.setModel(tabMode5);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbPermintaanStok.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbPermintaanStok.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i <12; i++) {
            TableColumn column = tbPermintaanStok.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(85);
            }else if(i==1){
                column.setPreferredWidth(80);
            }else if(i==2){
                column.setPreferredWidth(85);
            }else if(i==3){
                column.setPreferredWidth(105);
            }else if(i==4){
                column.setPreferredWidth(70);
            }else if(i==5){
                column.setPreferredWidth(190);
            }else if(i==6){
                column.setPreferredWidth(190);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==9){
                column.setPreferredWidth(140);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==11){
                column.setPreferredWidth(120);
            }
        }
        tbPermintaanStok.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabMode6=new DefaultTableModel(null,new Object[]{
                "No.Permintaan","Tanggal","Ruang/Kamar","Status","Pasien","Dokter Yang Meminta"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbDetailPermintaanStok.setModel(tabMode6);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbDetailPermintaanStok.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbDetailPermintaanStok.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 6; i++) {
            TableColumn column = tbDetailPermintaanStok.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(85);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(140);
            }else if(i==3){
                column.setPreferredWidth(100);
            }else if(i==4){
                column.setPreferredWidth(350);
            }else if(i==5){
                column.setPreferredWidth(270);
            }
        }
        tbDetailPermintaanStok.setDefaultRenderer(Object.class, new WarnaTable());
        
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        }
        
        dlgobt.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                tampil();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        dlgobt2.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                tampil3();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        dlgstok.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                tampil5();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {;}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){ 
                    if(TabPilihRawat.getSelectedIndex()==0){
                        CrDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        CrDokter.requestFocus();
                    }else if(TabPilihRawat.getSelectedIndex()==1){
                        CrDokter2.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        CrDokter2.requestFocus();
                    }                        
                }                
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        poli.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(poli.getTable().getSelectedRow()!= -1){   
                    CrPoli.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(),1).toString());
                    CrPoli.requestFocus();
                }   
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        }); 
        
        ruang.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(ruang.getTable().getSelectedRow()!= -1){   
                    Kamar.setText(ruang.getTable().getValueAt(ruang.getTable().getSelectedRow(),1).toString());  
                    Kamar.requestFocus();
                }                      
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        try {
            aktifkanparsial=koneksiDB.AKTIFKANBILLINGPARSIAL();
            alarm=koneksiDB.ALARMAPOTEK();
            formalarm=koneksiDB.FORMALARMAPOTEK();
        } catch (Exception ex) {
            aktifkanparsial="no";
            alarm="no";
            formalarm="ralan + ranap";
        }
        
        if(alarm.equals("yes")){
            jam();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        BtnCetakEresep = new javax.swing.JMenuItem();
        BtnCetakEresepM = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        BtnCetakEresep1 = new javax.swing.JMenuItem();
        BtnCetakEresepM1 = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        jPanel2 = new javax.swing.JPanel();
        panelisi2 = new widget.panelisi();
        jLabel20 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel12 = new widget.Label();
        cmbStatus = new widget.ComboBox();
        label9 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        panelisi1 = new widget.panelisi();
        BtnTambah = new widget.Button();
        BtnEdit = new widget.Button();
        BtnHapus = new widget.Button();
        BtnPrint = new widget.Button();
        BtnRekap = new widget.Button();
        BtnAll = new widget.Button();
        label10 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        TabPilihRawat = new javax.swing.JTabbedPane();
        internalFrame2 = new widget.InternalFrame();
        TabRawatJalan = new javax.swing.JTabbedPane();
        scrollPane1 = new widget.ScrollPane();
        tbResepRalan = new widget.Table();
        scrollPane2 = new widget.ScrollPane();
        tbDetailResepRalan = new widget.Table();
        panelGlass8 = new widget.panelisi();
        jLabel14 = new widget.Label();
        CrDokter = new widget.TextBox();
        BtnSeek3 = new widget.Button();
        jLabel16 = new widget.Label();
        CrPoli = new widget.TextBox();
        BtnSeek4 = new widget.Button();
        internalFrame3 = new widget.InternalFrame();
        TabRawatInap = new javax.swing.JTabbedPane();
        scrollPane3 = new widget.ScrollPane();
        tbResepRanap = new widget.Table();
        scrollPane4 = new widget.ScrollPane();
        tbDetailResepRanap = new widget.Table();
        scrollPane5 = new widget.ScrollPane();
        tbPermintaanStok = new widget.Table();
        scrollPane6 = new widget.ScrollPane();
        tbDetailPermintaanStok = new widget.Table();
        panelGlass9 = new widget.panelisi();
        jLabel15 = new widget.Label();
        CrDokter2 = new widget.TextBox();
        BtnSeek5 = new widget.Button();
        jLabel17 = new widget.Label();
        Kamar = new widget.TextBox();
        BtnSeek6 = new widget.Button();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        BtnCetakEresep.setBackground(new java.awt.Color(255, 255, 254));
        BtnCetakEresep.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        BtnCetakEresep.setForeground(new java.awt.Color(50, 50, 50));
        BtnCetakEresep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        BtnCetakEresep.setText("Cetak E-Resep");
        BtnCetakEresep.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BtnCetakEresep.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BtnCetakEresep.setName("BtnCetakEresep"); // NOI18N
        BtnCetakEresep.setPreferredSize(new java.awt.Dimension(200, 26));
        BtnCetakEresep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCetakEresepActionPerformed(evt);
            }
        });
        jPopupMenu1.add(BtnCetakEresep);

        BtnCetakEresepM.setBackground(new java.awt.Color(255, 255, 254));
        BtnCetakEresepM.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        BtnCetakEresepM.setForeground(new java.awt.Color(50, 50, 50));
        BtnCetakEresepM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        BtnCetakEresepM.setText("Cetak Manual");
        BtnCetakEresepM.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BtnCetakEresepM.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BtnCetakEresepM.setName("BtnCetakEresepM"); // NOI18N
        BtnCetakEresepM.setPreferredSize(new java.awt.Dimension(200, 26));
        BtnCetakEresepM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCetakEresepMActionPerformed(evt);
            }
        });
        jPopupMenu1.add(BtnCetakEresepM);

        jPopupMenu2.setName("jPopupMenu2"); // NOI18N

        BtnCetakEresep1.setBackground(new java.awt.Color(255, 255, 254));
        BtnCetakEresep1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        BtnCetakEresep1.setForeground(new java.awt.Color(50, 50, 50));
        BtnCetakEresep1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        BtnCetakEresep1.setText("Cetak E-Resep");
        BtnCetakEresep1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BtnCetakEresep1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BtnCetakEresep1.setName("BtnCetakEresep1"); // NOI18N
        BtnCetakEresep1.setPreferredSize(new java.awt.Dimension(200, 26));
        BtnCetakEresep1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCetakEresep1ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(BtnCetakEresep1);

        BtnCetakEresepM1.setBackground(new java.awt.Color(255, 255, 254));
        BtnCetakEresepM1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        BtnCetakEresepM1.setForeground(new java.awt.Color(50, 50, 50));
        BtnCetakEresepM1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        BtnCetakEresepM1.setText("Cetak Manual");
        BtnCetakEresepM1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BtnCetakEresepM1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BtnCetakEresepM1.setName("BtnCetakEresepM1"); // NOI18N
        BtnCetakEresepM1.setPreferredSize(new java.awt.Dimension(200, 26));
        BtnCetakEresepM1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCetakEresepM1ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(BtnCetakEresepM1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Peresepan Obat Oleh Dokter ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(816, 100));
        jPanel2.setLayout(new java.awt.BorderLayout(1, 1));

        panelisi2.setBackground(new java.awt.Color(255, 150, 255));
        panelisi2.setName("panelisi2"); // NOI18N
        panelisi2.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        jLabel20.setText("Tgl.Peresepan :");
        jLabel20.setName("jLabel20"); // NOI18N
        jLabel20.setPreferredSize(new java.awt.Dimension(85, 23));
        panelisi2.add(jLabel20);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "16-06-2022" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelisi2.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(24, 23));
        panelisi2.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "16-06-2022" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelisi2.add(DTPCari2);

        jLabel12.setText("Status :");
        jLabel12.setName("jLabel12"); // NOI18N
        jLabel12.setPreferredSize(new java.awt.Dimension(60, 23));
        panelisi2.add(jLabel12);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Belum Terlayani", "Sudah Terlayani" }));
        cmbStatus.setName("cmbStatus"); // NOI18N
        cmbStatus.setPreferredSize(new java.awt.Dimension(130, 23));
        panelisi2.add(cmbStatus);

        label9.setText("Key Word :");
        label9.setName("label9"); // NOI18N
        label9.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi2.add(label9);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(240, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelisi2.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('1');
        BtnCari.setToolTipText("Alt+1");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelisi2.add(BtnCari);

        jPanel2.add(panelisi2, java.awt.BorderLayout.PAGE_START);

        panelisi1.setName("panelisi1"); // NOI18N
        panelisi1.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambah.setMnemonic('S');
        BtnTambah.setText("Validasi");
        BtnTambah.setToolTipText("Alt+S");
        BtnTambah.setName("BtnTambah"); // NOI18N
        BtnTambah.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahActionPerformed(evt);
            }
        });
        BtnTambah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnTambahKeyPressed(evt);
            }
        });
        panelisi1.add(BtnTambah);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('U');
        BtnEdit.setText("Ubah");
        BtnEdit.setToolTipText("Alt+U");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelisi1.add(BtnEdit);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelisi1.add(BtnHapus);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelisi1.add(BtnPrint);

        BtnRekap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/preview-16x16.png"))); // NOI18N
        BtnRekap.setMnemonic('R');
        BtnRekap.setText("Rekap");
        BtnRekap.setToolTipText("Alt+R");
        BtnRekap.setName("BtnRekap"); // NOI18N
        BtnRekap.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnRekap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRekapActionPerformed(evt);
            }
        });
        BtnRekap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnRekapKeyPressed(evt);
            }
        });
        panelisi1.add(BtnRekap);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelisi1.add(BtnAll);

        label10.setText("Record :");
        label10.setName("label10"); // NOI18N
        label10.setPreferredSize(new java.awt.Dimension(60, 23));
        panelisi1.add(label10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(53, 23));
        panelisi1.add(LCount);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelisi1.add(BtnKeluar);

        jPanel2.add(panelisi1, java.awt.BorderLayout.CENTER);

        internalFrame1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        TabPilihRawat.setBackground(new java.awt.Color(255, 255, 253));
        TabPilihRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabPilihRawat.setName("TabPilihRawat"); // NOI18N
        TabPilihRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabPilihRawatMouseClicked(evt);
            }
        });

        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout());

        TabRawatJalan.setBackground(new java.awt.Color(255, 255, 253));
        TabRawatJalan.setForeground(new java.awt.Color(50, 50, 50));
        TabRawatJalan.setName("TabRawatJalan"); // NOI18N
        TabRawatJalan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatJalanMouseClicked(evt);
            }
        });

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        scrollPane1.setComponentPopupMenu(jPopupMenu1);
        scrollPane1.setName("scrollPane1"); // NOI18N
        scrollPane1.setOpaque(true);

        tbResepRalan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbResepRalan.setComponentPopupMenu(jPopupMenu1);
        tbResepRalan.setName("tbResepRalan"); // NOI18N
        tbResepRalan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbResepRalanMouseClicked(evt);
            }
        });
        tbResepRalan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbResepRalanKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(tbResepRalan);

        TabRawatJalan.addTab("Daftar Resep Rawat Jalan", scrollPane1);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        scrollPane2.setName("scrollPane2"); // NOI18N
        scrollPane2.setOpaque(true);

        tbDetailResepRalan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbDetailResepRalan.setName("tbDetailResepRalan"); // NOI18N
        scrollPane2.setViewportView(tbDetailResepRalan);

        TabRawatJalan.addTab("Detail Resep Rawat Jalan", scrollPane2);

        internalFrame2.add(TabRawatJalan, java.awt.BorderLayout.CENTER);

        panelGlass8.setBorder(null);
        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 41));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel14.setText("Dokter :");
        jLabel14.setName("jLabel14"); // NOI18N
        jLabel14.setPreferredSize(new java.awt.Dimension(55, 23));
        panelGlass8.add(jLabel14);

        CrDokter.setEditable(false);
        CrDokter.setName("CrDokter"); // NOI18N
        CrDokter.setPreferredSize(new java.awt.Dimension(305, 23));
        panelGlass8.add(CrDokter);

        BtnSeek3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeek3.setMnemonic('6');
        BtnSeek3.setToolTipText("ALt+6");
        BtnSeek3.setName("BtnSeek3"); // NOI18N
        BtnSeek3.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSeek3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeek3ActionPerformed(evt);
            }
        });
        panelGlass8.add(BtnSeek3);

        jLabel16.setText("Unit :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass8.add(jLabel16);

        CrPoli.setEditable(false);
        CrPoli.setName("CrPoli"); // NOI18N
        CrPoli.setPreferredSize(new java.awt.Dimension(305, 23));
        panelGlass8.add(CrPoli);

        BtnSeek4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeek4.setMnemonic('5');
        BtnSeek4.setToolTipText("ALt+5");
        BtnSeek4.setName("BtnSeek4"); // NOI18N
        BtnSeek4.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSeek4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeek4ActionPerformed(evt);
            }
        });
        panelGlass8.add(BtnSeek4);

        internalFrame2.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        TabPilihRawat.addTab("Rawat Jalan", internalFrame2);

        internalFrame3.setBorder(null);
        internalFrame3.setName("internalFrame3"); // NOI18N
        internalFrame3.setLayout(new java.awt.BorderLayout());

        TabRawatInap.setBackground(new java.awt.Color(255, 255, 253));
        TabRawatInap.setForeground(new java.awt.Color(50, 50, 50));
        TabRawatInap.setName("TabRawatInap"); // NOI18N
        TabRawatInap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatInapMouseClicked(evt);
            }
        });

        scrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        scrollPane3.setName("scrollPane3"); // NOI18N
        scrollPane3.setOpaque(true);

        tbResepRanap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbResepRanap.setComponentPopupMenu(jPopupMenu2);
        tbResepRanap.setName("tbResepRanap"); // NOI18N
        tbResepRanap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbResepRanapMouseClicked(evt);
            }
        });
        tbResepRanap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbResepRanapKeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(tbResepRanap);

        TabRawatInap.addTab("Daftar Resep Rawat Inap", scrollPane3);

        scrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        scrollPane4.setName("scrollPane4"); // NOI18N
        scrollPane4.setOpaque(true);

        tbDetailResepRanap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbDetailResepRanap.setName("tbDetailResepRanap"); // NOI18N
        scrollPane4.setViewportView(tbDetailResepRanap);

        TabRawatInap.addTab("Detail Resep Rawat Inap", scrollPane4);

        scrollPane5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        scrollPane5.setName("scrollPane5"); // NOI18N
        scrollPane5.setOpaque(true);

        tbPermintaanStok.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbPermintaanStok.setName("tbPermintaanStok"); // NOI18N
        tbPermintaanStok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPermintaanStokMouseClicked(evt);
            }
        });
        tbPermintaanStok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbPermintaanStokKeyPressed(evt);
            }
        });
        scrollPane5.setViewportView(tbPermintaanStok);

        TabRawatInap.addTab("Daftar Permintaan Stok", scrollPane5);

        scrollPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        scrollPane6.setName("scrollPane6"); // NOI18N
        scrollPane6.setOpaque(true);

        tbDetailPermintaanStok.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbDetailPermintaanStok.setName("tbDetailPermintaanStok"); // NOI18N
        scrollPane6.setViewportView(tbDetailPermintaanStok);

        TabRawatInap.addTab("Detail Permintaan Stok", scrollPane6);

        internalFrame3.add(TabRawatInap, java.awt.BorderLayout.CENTER);

        panelGlass9.setBorder(null);
        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 41));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel15.setText("Dokter :");
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(55, 23));
        panelGlass9.add(jLabel15);

        CrDokter2.setEditable(false);
        CrDokter2.setName("CrDokter2"); // NOI18N
        CrDokter2.setPreferredSize(new java.awt.Dimension(305, 23));
        panelGlass9.add(CrDokter2);

        BtnSeek5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeek5.setMnemonic('6');
        BtnSeek5.setToolTipText("ALt+6");
        BtnSeek5.setName("BtnSeek5"); // NOI18N
        BtnSeek5.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSeek5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeek5ActionPerformed(evt);
            }
        });
        panelGlass9.add(BtnSeek5);

        jLabel17.setText("Ruang/Kamar :");
        jLabel17.setName("jLabel17"); // NOI18N
        jLabel17.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel17);

        Kamar.setEditable(false);
        Kamar.setName("Kamar"); // NOI18N
        Kamar.setPreferredSize(new java.awt.Dimension(305, 23));
        panelGlass9.add(Kamar);

        BtnSeek6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeek6.setMnemonic('5');
        BtnSeek6.setToolTipText("ALt+5");
        BtnSeek6.setName("BtnSeek6"); // NOI18N
        BtnSeek6.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSeek6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeek6ActionPerformed(evt);
            }
        });
        panelGlass9.add(BtnSeek6);

        internalFrame3.add(panelGlass9, java.awt.BorderLayout.PAGE_END);

        TabPilihRawat.addTab("Rawat Inap", internalFrame3);

        internalFrame1.add(TabPilihRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            pilihTab();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            tbResepRalan.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        pilihTab();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            pilihTab();
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void tbResepRalanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbResepRalanMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
            if(evt.getClickCount()==2){
                if(akses.getberi_obat()==true){
                    BtnTambahActionPerformed(null);
                }
            }
        }
}//GEN-LAST:event_tbResepRalanMouseClicked

    private void tbResepRalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbResepRalanKeyPressed
        if(tabMode.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
                if(akses.getberi_obat()==true){
                    BtnTambahActionPerformed(null);
                }                    
            }
        }
}//GEN-LAST:event_tbResepRalanKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        if(TabPilihRawat.getSelectedIndex()==0){
            if(TabRawatJalan.getSelectedIndex()==0){
                if(tabMode.getRowCount()==0){            
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                   // BtnBatal.requestFocus();
                }else if(tabMode.getRowCount()!=0){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
                    Sequel.queryu("truncate table temporary_resep");
                    
                    for(int i=0;i<tabMode.getRowCount();i++){  
                        Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                            "0",tabMode.getValueAt(i,0).toString(),tabMode.getValueAt(i,1).toString(),tabMode.getValueAt(i,2).toString(),
                            tabMode.getValueAt(i,3).toString(),tabMode.getValueAt(i,4).toString(),tabMode.getValueAt(i,5).toString(),
                            tabMode.getValueAt(i,6).toString(),tabMode.getValueAt(i,7).toString(),tabMode.getValueAt(i,8).toString(),
                            tabMode.getValueAt(i,9).toString(),tabMode.getValueAt(i,10).toString(),tabMode.getValueAt(i,11).toString(),
                            "","","","","","","","","","","","","","","","","","","","","","","","","",""
                        });
                    }
                    
                    Map<String, Object> param = new HashMap<>();  
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());   
                    param.put("logo",Sequel.cariGambar("select logo from setting")); 
                    Valid.MyReport2("rptDaftarPermintaanResep.jasper","report","::[ Laporan Daftar Permintaan Resep ]::",param);
                    this.setCursor(Cursor.getDefaultCursor());
                }   
            }else if(TabRawatJalan.getSelectedIndex()==1){
                if(tabMode2.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    TCari.requestFocus();
                }else if(tabMode2.getRowCount()!=0){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
                    Sequel.queryu("truncate table temporary_resep");
                    
                    for(int i=0;i<tabMode2.getRowCount();i++){  
                        Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                            "0",tabMode2.getValueAt(i,0).toString(),tabMode2.getValueAt(i,1).toString(),tabMode2.getValueAt(i,2).toString(),
                            tabMode2.getValueAt(i,3).toString(),tabMode2.getValueAt(i,4).toString(),tabMode2.getValueAt(i,5).toString(),"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
                        });
                    }
                    
                    Map<String, Object> param = new HashMap<>();  
                        param.put("namars",akses.getnamars());
                        param.put("alamatrs",akses.getalamatrs());
                        param.put("kotars",akses.getkabupatenrs());
                        param.put("propinsirs",akses.getpropinsirs());
                        param.put("kontakrs",akses.getkontakrs());
                        param.put("emailrs",akses.getemailrs());   
                        param.put("logo",Sequel.cariGambar("select logo from setting")); 
                    Valid.MyReport2("rptDaftarResep.jasper","report","::[ Daftar Resep Obat ]::",param);
                    this.setCursor(Cursor.getDefaultCursor());
                }
            }
        }else if(TabPilihRawat.getSelectedIndex()==1){
            if(TabRawatInap.getSelectedIndex()==0){
                if(tabMode3.getRowCount()==0){            
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                   // BtnBatal.requestFocus();
                }else if(tabMode3.getRowCount()!=0){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    Sequel.queryu("truncate table temporary_resep");
                    
                    for(int i=0;i<tabMode3.getRowCount();i++){  
                        Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                            "0",tabMode3.getValueAt(i,0).toString(),tabMode3.getValueAt(i,1).toString(),tabMode3.getValueAt(i,2).toString(),
                            tabMode3.getValueAt(i,3).toString(),tabMode3.getValueAt(i,4).toString(),tabMode3.getValueAt(i,5).toString(),
                            tabMode3.getValueAt(i,6).toString(),tabMode3.getValueAt(i,7).toString(),tabMode3.getValueAt(i,8).toString(),
                            tabMode3.getValueAt(i,9).toString(),"",tabMode3.getValueAt(i,11).toString(),
                            "","","","","","","","","","","","","","","","","","","","","","","","","",""
                        });
                    }
                    
                    Map<String, Object> param = new HashMap<>();  
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());   
                    param.put("logo",Sequel.cariGambar("select logo from setting")); 
                    Valid.MyReport2("rptDaftarPermintaanResep2.jasper","report","::[ Laporan Daftar Permintaan Resep ]::",param);
                    this.setCursor(Cursor.getDefaultCursor());
                }   
            }else if(TabRawatInap.getSelectedIndex()==1){
                if(tabMode4.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    TCari.requestFocus();
                }else if(tabMode4.getRowCount()!=0){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
                    Sequel.queryu("truncate table temporary_resep");
                    
                    for(int i=0;i<tabMode4.getRowCount();i++){  
                        Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                            "0",tabMode4.getValueAt(i,0).toString(),tabMode4.getValueAt(i,1).toString(),tabMode4.getValueAt(i,2).toString(),
                            tabMode4.getValueAt(i,3).toString(),tabMode4.getValueAt(i,4).toString(),tabMode4.getValueAt(i,5).toString(),"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
                        });
                    }
                    
                    Map<String, Object> param = new HashMap<>();  
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());   
                    param.put("logo",Sequel.cariGambar("select logo from setting")); 
                    Valid.MyReport2("rptDaftarResep2.jasper","report","::[ Daftar Resep Obat ]::",param);
                    this.setCursor(Cursor.getDefaultCursor());
                }
            }else if(TabRawatInap.getSelectedIndex()==2){
                if(tabMode5.getRowCount()==0){            
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                   // BtnBatal.requestFocus();
                }else if(tabMode5.getRowCount()!=0){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    Sequel.queryu("truncate table temporary_resep");
                    
                    for(int i=0;i<tabMode5.getRowCount();i++){  
                        Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                            "0",tabMode5.getValueAt(i,0).toString(),tabMode5.getValueAt(i,1).toString(),tabMode5.getValueAt(i,2).toString(),
                            tabMode5.getValueAt(i,3).toString(),tabMode5.getValueAt(i,4).toString(),tabMode5.getValueAt(i,5).toString(),
                            tabMode5.getValueAt(i,6).toString(),tabMode5.getValueAt(i,7).toString(),tabMode5.getValueAt(i,8).toString(),
                            tabMode5.getValueAt(i,9).toString(),"",tabMode5.getValueAt(i,11).toString(),
                            "","","","","","","","","","","","","","","","","","","","","","","","","",""
                        });
                    }
                    
                    Map<String, Object> param = new HashMap<>();  
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());   
                    param.put("logo",Sequel.cariGambar("select logo from setting")); 
                    Valid.MyReport2("rptDaftarPermintaanResep3.jasper","report","::[ Laporan Daftar Permintaan Stok Pasien ]::",param);
                    this.setCursor(Cursor.getDefaultCursor());
                }   
            }else if(TabRawatInap.getSelectedIndex()==3){
                if(tabMode6.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    TCari.requestFocus();
                }else if(tabMode6.getRowCount()!=0){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
                    Sequel.queryu("truncate table temporary_resep");
                    
                    for(int i=0;i<tabMode6.getRowCount();i++){  
                        Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                            "0",tabMode6.getValueAt(i,0).toString(),tabMode6.getValueAt(i,1).toString(),tabMode6.getValueAt(i,2).toString(),
                            tabMode6.getValueAt(i,3).toString(),tabMode6.getValueAt(i,4).toString(),tabMode6.getValueAt(i,5).toString(),"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
                        });
                    }
                    
                    Map<String, Object> param = new HashMap<>();  
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());   
                    param.put("logo",Sequel.cariGambar("select logo from setting")); 
                    Valid.MyReport2("rptDaftarResep3.jasper","report","::[ Daftar Permintaan Stok Obat Pasien ]::",param);
                    this.setCursor(Cursor.getDefaultCursor());
                }
            }
        }        
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            //Valid.pindah(evt,BtnEdit,BtnAll);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        if(TabPilihRawat.getSelectedIndex()==0){
            CrDokter.setText("");
            CrPoli.setText("");
            TCari.setText("");
            pilihRalan();
        }else if(TabPilihRawat.getSelectedIndex()==1){
            CrDokter2.setText("");
            Kamar.setText("");
            TCari.setText("");
            pilihRanap();
        }
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnPrint, BtnKeluar);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
            dispose();  
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){            
            dispose();              
        }else{Valid.pindah(evt,BtnAll,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahActionPerformed
        if(TabPilihRawat.getSelectedIndex()==0){
            if(TabRawatJalan.getSelectedIndex()==0){
                if(akses.getberi_obat()==true){
                    if(tabMode.getRowCount()==0){
                        JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                        TCari.requestFocus();
                    }else if(NoRawat.equals("")){
                        JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data resep dokter yang mau divalidasi..!!");
                    }else{
                        if(Status.equals("Sudah Terlayani")){
                            JOptionPane.showMessageDialog(rootPane,"Resep sudah tervalidasi ..!!");
                        }else {
                            jmlparsial=0;
                            if(aktifkanparsial.equals("yes")){
                                jmlparsial=Sequel.cariInteger("select count(kd_pj) from set_input_parsial where kd_pj=?",Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",NoRawat));
                            }
                            if(jmlparsial>0){
                                panggilform();
                            }else{
                                if(Sequel.cariRegistrasi(NoRawat)>0){
                                    JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi ..!!");
                                }else{ 
                                    panggilform();                             
                                }
                            }               
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Maaf, Anda tidak punya hak akses untuk mengvalidasi...!!!!");
                    TCari.requestFocus();
                }
            }else if(TabRawatJalan.getSelectedIndex()==1){
                JOptionPane.showMessageDialog(null,"Maaf, silahkan buka Daftar Resep...!!!!");
                TCari.requestFocus();
            }
        }else if(TabPilihRawat.getSelectedIndex()==1){
            if(TabRawatInap.getSelectedIndex()==0){
                if(akses.getberi_obat()==true){
                    if(tabMode3.getRowCount()==0){
                        JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                        TCari.requestFocus();
                    }else if(NoRawat.equals("")){
                        JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data resep dokter yang mau divalidasi..!!");
                    }else{
                        if(Status.equals("Sudah Terlayani")){
                            JOptionPane.showMessageDialog(rootPane,"Resep sudah tervalidasi ..!!");
                        }else {                           
                            if(Sequel.cariRegistrasi(NoRawat)>0){
                                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi ..!!");
                            }else{ 
                                panggilform2();                             
                            }                
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Maaf, Anda tidak punya hak akses untuk mengvalidasi...!!!!");
                    TCari.requestFocus();
                }
            }else if(TabRawatInap.getSelectedIndex()==1){
                JOptionPane.showMessageDialog(null,"Maaf, silahkan buka Daftar Resep...!!!!");
                TCari.requestFocus();
            }else if(TabRawatInap.getSelectedIndex()==2){
                if(akses.getstok_obat_pasien()==true){
                    if(tabMode5.getRowCount()==0){
                        JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                        TCari.requestFocus();
                    }else if(NoRawat.equals("")){
                        JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data permintaan stok pasien yang mau divalidasi..!!");
                    }else{
                        if(Status.equals("Sudah Terlayani")){
                            JOptionPane.showMessageDialog(rootPane,"Permintaan Stok Pasien sudah tervalidasi ..!!");
                        }else {                           
                            if(Sequel.cariRegistrasi(NoRawat)>0){
                                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi ..!!");
                            }else{ 
                                panggilform3();                             
                            }                
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Maaf, Anda tidak punya hak akses untuk mengvalidasi...!!!!");
                    TCari.requestFocus();
                }
            } else if(TabRawatInap.getSelectedIndex()==3){
                JOptionPane.showMessageDialog(null,"Maaf, silahkan buka Daftar Permintaan Stok...!!!!");
                TCari.requestFocus();
            }    
        }            
}//GEN-LAST:event_BtnTambahActionPerformed

    private void BtnTambahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnTambahKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnTambahActionPerformed(null);
        }else{
           Valid.pindah(evt,BtnEdit,BtnKeluar);
        }
}//GEN-LAST:event_BtnTambahKeyPressed
/*
private void KdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKdKeyPressed
    Valid.pindah(evt,BtnCari,Nm);
}//GEN-LAST:event_TKdKeyPressed
*/

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        pilihTab();
    }//GEN-LAST:event_formWindowOpened

    private void TabRawatJalanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatJalanMouseClicked
        pilihRalan();
    }//GEN-LAST:event_TabRawatJalanMouseClicked

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TabPilihRawat.getSelectedIndex()==0){
            if(TabRawatJalan.getSelectedIndex()==0){
                if(tabMode.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TCari.requestFocus();
                }else if(NoRawat.equals("")){
                    JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data resep dokter yang mau divalidasi..!!");
                }else{
                    if(Status.equals("Sudah Terlayani")){
                        JOptionPane.showMessageDialog(rootPane,"Resep sudah tervalidasi ..!!");
                    }else {
                        DlgPeresepanDokter resep=new DlgPeresepanDokter(null,false);
                        resep.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
                        resep.setLocationRelativeTo(internalFrame1);
                        resep.MatikanJam();
                        resep.setNoRm(NoRawat,Valid.SetTgl2(TglPeresepan),JamPeresepan.substring(0,2),JamPeresepan.substring(3,5),JamPeresepan.substring(6,8),KodeDokter,DokterPeresep,"ralan");
                        resep.isCek();
                        resep.tampilobat(NoResep);
                        TeksKosong();
                        resep.setVisible(true);                         
                    }                    
                }
            }else if(TabRawatJalan.getSelectedIndex()==1){
                JOptionPane.showMessageDialog(null,"Maaf, silahkan buka Daftar Resep...!!!!");
                TCari.requestFocus();
            }
        }else if(TabPilihRawat.getSelectedIndex()==1){
            if(TabRawatInap.getSelectedIndex()==0){
                if(tabMode3.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TCari.requestFocus();
                }else if(NoRawat.equals("")){
                    JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data resep dokter yang mau divalidasi..!!");
                }else{
                    if(Status.equals("Sudah Terlayani")){
                        JOptionPane.showMessageDialog(rootPane,"Resep sudah tervalidasi ..!!");
                    }else {
                        kamar=Sequel.cariIsi("select kd_bangsal from kamar inner join kamar_inap on kamar_inap.kd_kamar=kamar.kd_kamar "+
                                "where kamar_inap.no_rawat=? order by kamar_inap.tgl_masuk desc limit 1 ",NoRawat); 
                        bangsal=Sequel.cariIsi("select kd_depo from set_depo_ranap where kd_bangsal=?",kamar);
                        if(bangsal.equals("")){
                            if(Sequel.cariIsi("select asal_stok from set_lokasi").equals("Gunakan Stok Bangsal")){
                                akses.setkdbangsal(kamar);
                            }else{
                                akses.setkdbangsal(Sequel.cariIsi("select kd_bangsal from set_lokasi"));
                            }
                        }else{
                            akses.setkdbangsal(bangsal);
                        }
                        DlgPeresepanDokter resep=new DlgPeresepanDokter(null,false);
                        resep.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                        resep.setLocationRelativeTo(internalFrame1);
                        resep.MatikanJam();
                        resep.setNoRm(NoRawat,Valid.SetTgl2(TglPeresepan),JamPeresepan.substring(0,2),JamPeresepan.substring(3,5),JamPeresepan.substring(6,8),KodeDokter,DokterPeresep,"ranap");
                        resep.isCek();
                        resep.tampilobat(NoResep);
                        TeksKosong();
                        resep.setVisible(true);                          
                    }                    
                }
            }else if(TabRawatInap.getSelectedIndex()==1){
                JOptionPane.showMessageDialog(null,"Maaf, silahkan buka Daftar Resep...!!!!");
                TCari.requestFocus();
            } 
        }
            
    }//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnTambah, BtnPrint);
        }
    }//GEN-LAST:event_BtnEditKeyPressed

    private void BtnRekapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRekapActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DlgResepObat resep=new DlgResepObat(null,false);
        resep.tampil();
        resep.emptTeks();
        resep.isCek();
        resep.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        resep.setLocationRelativeTo(internalFrame1);
        resep.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnRekapActionPerformed

    private void BtnRekapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnRekapKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnRekapKeyPressed

    private void TabPilihRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabPilihRawatMouseClicked
        pilihTab();
    }//GEN-LAST:event_TabPilihRawatMouseClicked

    private void tbResepRanapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbResepRanapMouseClicked
        if(tabMode3.getRowCount()!=0){
            try {
                getData2();
            } catch (java.lang.NullPointerException e) {
            }
            if(evt.getClickCount()==2){
                if(akses.getberi_obat()==true){
                    BtnTambahActionPerformed(null);
                }
            }
        }
    }//GEN-LAST:event_tbResepRanapMouseClicked

    private void tbResepRanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbResepRanapKeyPressed
        if(tabMode3.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    getData2();
                } catch (java.lang.NullPointerException e) {
                }
                if(akses.getberi_obat()==true){
                    BtnTambahActionPerformed(null);
                }                    
            }
        }
    }//GEN-LAST:event_tbResepRanapKeyPressed

    private void TabRawatInapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatInapMouseClicked
        pilihRanap();
    }//GEN-LAST:event_TabRawatInapMouseClicked

    private void BtnSeek3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeek3ActionPerformed
        dokter.isCek();
        dokter.TCari.requestFocus();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnSeek3ActionPerformed

    private void BtnSeek4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeek4ActionPerformed
        poli.isCek();
        poli.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        poli.setLocationRelativeTo(internalFrame1);
        poli.setVisible(true);
    }//GEN-LAST:event_BtnSeek4ActionPerformed

    private void BtnSeek5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeek5ActionPerformed
        dokter.isCek();
        dokter.TCari.requestFocus();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnSeek5ActionPerformed

    private void BtnSeek6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeek6ActionPerformed
        ruang.isCek();
        ruang.emptTeks();        
        ruang.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        ruang.setLocationRelativeTo(internalFrame1);
        ruang.setVisible(true);
    }//GEN-LAST:event_BtnSeek6ActionPerformed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(TabPilihRawat.getSelectedIndex()==0){
            if(TabRawatJalan.getSelectedIndex()==0){
                if(akses.getresep_dokter()==true){
                    if(tabMode.getRowCount()==0){
                        JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                        TCari.requestFocus();
                    }else if(NoRawat.equals("")){
                        JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data resep dokter yang mau dihapus..!!");
                    }else{
                        if(Status.equals("Sudah Terlayani")){
                            JOptionPane.showMessageDialog(rootPane,"Resep sudah tervalidasi ..!!");
                        }else {
                            int reply = JOptionPane.showConfirmDialog(rootPane,"Eeiiiiiits, udah bener belum data yang mau dihapus..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
                            if (reply == JOptionPane.YES_OPTION) { 
                                Sequel.meghapus("resep_obat","no_resep",NoResep);    
                                TeksKosong();
                                tampil();
                            }
                        }                    
                    }
                }
            }else if(TabRawatJalan.getSelectedIndex()==1){
                JOptionPane.showMessageDialog(null,"Maaf, silahkan buka Daftar Resep...!!!!");
                TCari.requestFocus();
            }
        }else if(TabPilihRawat.getSelectedIndex()==1){
            if(TabRawatInap.getSelectedIndex()==0){
                if(akses.getresep_dokter()==true){
                    if(tabMode3.getRowCount()==0){
                        JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                        TCari.requestFocus();
                    }else if(NoRawat.equals("")){
                        JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data resep dokter yang mau dihapus..!!");
                    }else{
                        if(Status.equals("Sudah Terlayani")){
                            JOptionPane.showMessageDialog(rootPane,"Resep sudah tervalidasi ..!!");
                        }else {
                            Sequel.meghapus("resep_obat","no_resep",NoResep); 
                            TeksKosong();
                            tampil3();
                        }                    
                    }
                }
            }else if(TabRawatInap.getSelectedIndex()==1){
                JOptionPane.showMessageDialog(null,"Maaf, silahkan buka Daftar Resep...!!!!");
                TCari.requestFocus();
            }else if(TabRawatInap.getSelectedIndex()==2){
                if(akses.getpermintaan_stok_obat_pasien()==true){
                    if(tabMode5.getRowCount()==0){
                        JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                        TCari.requestFocus();
                    }else if(NoRawat.equals("")){
                        JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data permintaan stok yang mau dihapus..!!");
                    }else{
                        if(Status.equals("Sudah Terlayani")){
                            JOptionPane.showMessageDialog(rootPane,"permintaan stok sudah tervalidasi ..!!");
                        }else {
                            Sequel.meghapus("permintaan_stok_obat_pasien","no_permintaan",NoResep); 
                            TeksKosong();
                            tampil5();
                        }                    
                    }
                }
            } else if(TabRawatInap.getSelectedIndex()==3){
                JOptionPane.showMessageDialog(null,"Maaf, silahkan buka Daftar Permintaan Stok...!!!!");
                TCari.requestFocus();
            }  
        }
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnHapusActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnHapus, BtnEdit);
        }
    }//GEN-LAST:event_BtnHapusKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        aktif=true;
    }//GEN-LAST:event_formWindowActivated

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        aktif=false;
    }//GEN-LAST:event_formWindowDeactivated

    private void tbPermintaanStokMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPermintaanStokMouseClicked
        if(tabMode5.getRowCount()!=0){
            try {
                getData3();
            } catch (java.lang.NullPointerException e) {
            }
            if(evt.getClickCount()==2){
                if(akses.getstok_obat_pasien()==true){
                    BtnTambahActionPerformed(null);
                }
            }
        }
    }//GEN-LAST:event_tbPermintaanStokMouseClicked

    private void tbPermintaanStokKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPermintaanStokKeyPressed
        if(tabMode5.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    getData3();
                } catch (java.lang.NullPointerException e) {
                }
                if(akses.getstok_obat_pasien()==true){
                    BtnTambahActionPerformed(null);
                }                    
            }
        }
    }//GEN-LAST:event_tbPermintaanStokKeyPressed

    private void BtnCetakEresepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCetakEresepActionPerformed
        // Isi Disini Untuk Cetak EResep
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TCari.requestFocus();
        }else if(tbResepRalan.getSelectedRow()<= -1){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data..!!");
        } else {
            // Tambah Query Untuk Report
            try{
                //Umum
                Sequel.queryu("truncate table temporary_resep");
                ps4=koneksi.prepareStatement(
                        "select databarang.nama_brng, resep_dokter.jml, resep_dokter.aturan_pakai " +
                        "from resep_dokter " +
                        "inner join databarang " +
                        "on databarang.kode_brng = resep_dokter.kode_brng " +
                        "where resep_dokter.no_resep =? " +
                        "order by databarang.nama_brng");
                try{
                    ps4.setString(1, tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),0).toString());
                    rs4=ps4.executeQuery();
                    while(rs4.next()){
                        namaobat = rs4.getString("nama_brng");
                        Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                            "0",namaobat,rs4.getString("aturan_pakai"),"No. "+intToRoman(rs4.getInt("jml")),"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
                        });
                    }
                } catch (Exception e) {
                    System.out.println("Notif Resep Umum = "+e);
                } finally{
                    if(rs4!=null){
                        rs4.close();
                    }
                    if(ps4!=null){
                        ps4.close();
                    }
                }

                //Racikan
                psracikan=koneksi.prepareStatement(
                        "select " +
                        "resep_dokter_racikan.no_racik, " +
                        "metode_racik.nm_racik, " +
                        "resep_dokter_racikan.jml_dr, " +
                        "resep_dokter_racikan.aturan_pakai, " +
                        "resep_dokter_racikan.keterangan " +
                        "from resep_dokter_racikan inner join metode_racik on metode_racik.kd_racik = resep_dokter_racikan.kd_racik " +
                        "where resep_dokter_racikan.no_resep = ?" +
                        "order by no_racik");
                try{
                    psracikan.setString(1, tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),0).toString());
                    rsracikan=psracikan.executeQuery();
                    while(rsracikan.next()){
                        rincianobat = "";
                        psracikan2=koneksi.prepareStatement(
                                "select databarang.nama_brng, resep_dokter_racikan_detail.p1,resep_dokter_racikan_detail.p2,resep_dokter_racikan_detail.kandungan " +
                                "from resep_dokter_racikan_detail " +
                                "inner join databarang " +
                                "on databarang.kode_brng = resep_dokter_racikan_detail.kode_brng " +
                                "where resep_dokter_racikan_detail.no_racik=? and resep_dokter_racikan_detail.no_resep=? " +
                                "order by databarang.nama_brng"
                                 );
                        try{
                            psracikan2.setString(1, rsracikan.getString("no_racik"));
                            psracikan2.setString(2, tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),0).toString());
                            rsracikan2 = psracikan2.executeQuery();
                            while(rsracikan2.next()){
                                namaobat1 = rsracikan2.getString("nama_brng");
                                rincianobat=namaobat1+" "+rsracikan2.getString("kandungan")+"\n"+rincianobat;
                            }
                        }catch (Exception e){
                            System.out.println("Notifikasi Racikan 1 = "+e);
                        } finally {
                            if(rsracikan2!=null){
                                rsracikan2.close();
                            }
                            if(psracikan2!=null){
                                psracikan2.close();
                            }
                        }

                        rincianobat = rincianobat.substring(0,rincianobat.length() - 1);

                if(rsracikan.getString("nm_racik").equals("Puyer")){
                    latin_racikan = "mfla pulv dtd";
                }else if(rsracikan.getString("nm_racik").equals("Capsul")){
                    latin_racikan = "mfla pulv da in cap";
                }else if(rsracikan.getString("nm_racik").equals("Cream")){
                    latin_racikan = "mfla cream da in pot";
                }else if(rsracikan.getString("nm_racik").equals("Gel")){
                    latin_racikan = "mfla gel da in pot";
                }

                Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                    "0",rincianobat,rsracikan.getString("aturan_pakai"),"","",rsracikan.getString("keterangan"),"No. "+intToRoman(rsracikan.getInt("jml_dr")),latin_racikan,"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
                });

                }

                } catch (Exception e) {
                    System.out.println("Notif Resep Detail Racikan = "+e);
                } finally{
                    if(rsracikan!=null){
                        rsracikan.close();
                    }
                    if(psracikan!=null){
                        psracikan.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif Cetak E-Resep = "+e);
            }

            Map<String, Object> param = new HashMap<>();  
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("emailrs",akses.getemailrs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("penanggung",Sequel.cariIsi("select png_jawab from penjab where kd_pj=?",Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),3).toString())));               
            param.put("propinsirs",akses.getpropinsirs());
            param.put("tanggal",Valid.SetTgl3(tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),1).toString()+""));
            param.put("norawat",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),3).toString());
            param.put("pasien",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),5).toString());
            param.put("norm",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),4).toString());
            param.put("peresep",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),6).toString());
            param.put("noresep",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),0).toString());
            bb=Sequel.cariIsi("select berat from pemeriksaan_ralan where no_rawat=?",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),3).toString());
            if(bb.equalsIgnoreCase("")){
                param.put("bb","-");
            }else{
                param.put("bb",bb);
            }
            alergi=Sequel.cariIsi("select alergi from pemeriksaan_ralan where no_rawat=?",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),3).toString());
            param.put("alergi",alergi);
            tgl_lahir=Sequel.cariIsi("select tgl_lahir from pasien where no_rkm_medis =?",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),4).toString());
            param.put("tgl_lahir",tgl_lahir);
            finger=Sequel.cariIsi("select sha1(sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),8).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),6).toString()+"\nID "+(finger.equals("")?tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),8).toString():finger)+"\n"+tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),1).toString());  
            param.put("jam",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),2).toString());
            param.put("logo",Sequel.cariGambar("select logo from setting")); 

            Valid.MyReport("rptEResep.jasper",param,"::[ Lembar Pemberian Obat Resep Dokter]::");
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnCetakEresepActionPerformed

    private void BtnCetakEresep1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCetakEresep1ActionPerformed
        // Isi Disini Untuk Cetak EResep
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TCari.requestFocus();
        }else if(tbResepRanap.getSelectedRow()<= -1){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data..!!");
        } else {
            // Tambah Query Untuk Report
            try{
                //Umum
                Sequel.queryu("truncate table temporary_resep");
                ps4=koneksi.prepareStatement(
                        "select databarang.nama_brng, resep_dokter.jml, resep_dokter.aturan_pakai " +
                        "from resep_dokter " +
                        "inner join databarang " +
                        "on databarang.kode_brng = resep_dokter.kode_brng " +
                        "where resep_dokter.no_resep =? " +
                        "order by databarang.nama_brng");
                try{
                    ps4.setString(1, tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),0).toString());
                    rs4=ps4.executeQuery();
                    while(rs4.next()){
                        namaobat = rs4.getString("nama_brng");
                        Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                            "0",namaobat,rs4.getString("aturan_pakai"),"No. "+intToRoman(rs4.getInt("jml")),"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
                        });
                    }
                } catch (Exception e) {
                    System.out.println("Notif Resep Umum = "+e);
                } finally{
                    if(rs4!=null){
                        rs4.close();
                    }
                    if(ps4!=null){
                        ps4.close();
                    }
                }

                //Racikan
                psracikan=koneksi.prepareStatement(
                        "select " +
                        "resep_dokter_racikan.no_racik, " +
                        "metode_racik.nm_racik, " +
                        "resep_dokter_racikan.jml_dr, " +
                        "resep_dokter_racikan.aturan_pakai, " +
                        "resep_dokter_racikan.keterangan " +
                        "from resep_dokter_racikan inner join metode_racik on metode_racik.kd_racik = resep_dokter_racikan.kd_racik " +
                        "where resep_dokter_racikan.no_resep = ?" +
                        "order by no_racik");
                try{
                    psracikan.setString(1, tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),0).toString());
                    rsracikan=psracikan.executeQuery();
                    while(rsracikan.next()){
                        rincianobat = "";
                        psracikan2=koneksi.prepareStatement(
                                "select databarang.nama_brng, resep_dokter_racikan_detail.p1,resep_dokter_racikan_detail.p2,resep_dokter_racikan_detail.kandungan " +
                                "from resep_dokter_racikan_detail " +
                                "inner join databarang " +
                                "on databarang.kode_brng = resep_dokter_racikan_detail.kode_brng " +
                                "where resep_dokter_racikan_detail.no_racik=? and resep_dokter_racikan_detail.no_resep=? " +
                                "order by databarang.nama_brng"
                                 );
                        try{
                            psracikan2.setString(1, rsracikan.getString("no_racik"));
                            psracikan2.setString(2, tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),0).toString());
                            rsracikan2 = psracikan2.executeQuery();
                            while(rsracikan2.next()){
                                namaobat1 = rsracikan2.getString("nama_brng");
                                rincianobat=namaobat1+" "+rsracikan2.getString("kandungan")+"\n"+rincianobat;
                            }
                        }catch (Exception e){
                            System.out.println("Notifikasi Racikan 1 = "+e);
                        } finally {
                            if(rsracikan2!=null){
                                rsracikan2.close();
                            }
                            if(psracikan2!=null){
                                psracikan2.close();
                            }
                        }

                        rincianobat = rincianobat.substring(0,rincianobat.length() - 1);

                if(rsracikan.getString("nm_racik").equals("Puyer")){
                    latin_racikan = "mfla pulv dtd";
                }else if(rsracikan.getString("nm_racik").equals("Capsul")){
                    latin_racikan = "mfla pulv da in cap";
                }else if(rsracikan.getString("nm_racik").equals("Cream")){
                    latin_racikan = "mfla cream da in pot";
                }else if(rsracikan.getString("nm_racik").equals("Gel")){
                    latin_racikan = "mfla gel da in pot";
                }

                Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                    "0",rincianobat,rsracikan.getString("aturan_pakai"),"","",rsracikan.getString("keterangan"),"No. "+intToRoman(rsracikan.getInt("jml_dr")),latin_racikan,"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
                });

                }

                } catch (Exception e) {
                    System.out.println("Notif Resep Detail Racikan = "+e);
                } finally{
                    if(rsracikan!=null){
                        rsracikan.close();
                    }
                    if(psracikan!=null){
                        psracikan.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif Cetak E-Resep = "+e);
            }

            Map<String, Object> param = new HashMap<>();  
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("emailrs",akses.getemailrs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("penanggung",Sequel.cariIsi("select png_jawab from penjab where kd_pj=?",Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),3).toString())));               
            param.put("propinsirs",akses.getpropinsirs());
            param.put("tanggal",Valid.SetTgl3(tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),1).toString()+""));
            param.put("norawat",tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),3).toString());
            param.put("pasien",tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),5).toString());
            param.put("norm",tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),4).toString());
            param.put("peresep",tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),6).toString());
            param.put("noresep",tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),0).toString());
            bb=Sequel.cariIsi("select berat from pemeriksaan_ralan where no_rawat=?",tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),3).toString());
            if(bb.equalsIgnoreCase("")){
                param.put("bb","-");
            }else{
                param.put("bb",bb);
            }
            alergi=Sequel.cariIsi("select alergi from pemeriksaan_ralan where no_rawat=?",tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),3).toString());
            param.put("alergi",alergi);
            tgl_lahir=Sequel.cariIsi("select tgl_lahir from pasien where no_rkm_medis =?",tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),4).toString());
            param.put("tgl_lahir",tgl_lahir);
            finger=Sequel.cariIsi("select sha1(sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),8).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),6).toString()+"\nID "+(finger.equals("")?tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),8).toString():finger)+"\n"+tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),1).toString());  
            param.put("jam",tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),2).toString());
            param.put("logo",Sequel.cariGambar("select logo from setting")); 

            Valid.MyReport("rptEResep.jasper",param,"::[ Lembar Pemberian Obat Resep Dokter]::");
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnCetakEresep1ActionPerformed

    private void BtnCetakEresepMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCetakEresepMActionPerformed
        // Isi Disini Untuk Cetak EResep
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TCari.requestFocus();
        }else if(tbResepRalan.getSelectedRow()<= -1){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan pilih data..!!");
        } else {
            // Tambah Query Untuk Report
            try{
                //Umum
                Sequel.queryu("truncate table temporary_resep");
                ps4=koneksi.prepareStatement(
                        "SELECT " +
                        "resep_dokter_rsmg.nm_obat,resep_dokter_rsmg.jml,resep_dokter_rsmg.aturan_pakai " +
                        "FROM resep_dokter_rsmg " +
                        "WHERE resep_dokter_rsmg.no_resep=?" +
                        "ORDER BY resep_dokter_rsmg.nm_obat ASC");
                try{
                    ps4.setString(1,tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),0).toString());
                    rs4=ps4.executeQuery();
                    while(rs4.next()){
                        namaobat = rs4.getString("nm_obat");
                        Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                            "0",namaobat,rs4.getString("aturan_pakai"),"No. "+intToRoman(rs4.getInt("jml")),"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
                        });
                    }
                } catch (Exception e) {
                    System.out.println("Notif Resep Umum = "+e);
                } finally{
                    if(rs4!=null){
                        rs4.close();
                    }
                    if(ps4!=null){
                        ps4.close();
                    }
                }

                //Racikan
                psracikan=koneksi.prepareStatement(
                    "select resep_dokter_racikan_rsmg.no_racikan,resep_dokter_racikan_rsmg.nama_racik,resep_dokter_racikan_rsmg.kd_racik, " +
                    "resep_dokter_racikan_rsmg.jml,resep_dokter_racikan_rsmg.aturan_pakai,resep_dokter_racikan_rsmg.keterangan " +
                    "from resep_dokter_racikan_rsmg " +
                    "where resep_dokter_racikan_rsmg.no_resep=? order by resep_dokter_racikan_rsmg.no_racikan ASC");
                try{
                    psracikan.setString(1,tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),0).toString());
                    rsracikan=psracikan.executeQuery();
                    while(rsracikan.next()){
                        rincianobat = "";
                        psracikan2=koneksi.prepareStatement(
                                "select resep_dokter_racikan_rsmg.no_racikan,resep_dokter_racikan_rsmg.nama_racik,resep_dokter_racikan_rsmg.kd_racik, " +
                                "resep_dokter_racikan_rsmg.jml,resep_dokter_racikan_rsmg.aturan_pakai,resep_dokter_racikan_rsmg.keterangan " +
                                "from resep_dokter_racikan_rsmg " +
                                "where resep_dokter_racikan_rsmg.no_racikan =? and resep_dokter_racikan_rsmg.no_resep =? order by resep_dokter_racikan_rsmg.nama_racik ASC"
                                 );
                        try{
                            psracikan2.setString(1, rsracikan.getString("no_racikan"));
                            psracikan2.setString(2, tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),0).toString());
                            rsracikan2 = psracikan2.executeQuery();
                            
                            while(rsracikan2.next()){
                                namaobat1 = rsracikan2.getString("nama_racik");
                            }
                            
                        }catch (Exception e){
                            System.out.println("Notifikasi Racikan 1 = "+e);
                        } finally {
                            if(rsracikan2!=null){
                                rsracikan2.close();
                            }
                            if(psracikan2!=null){
                                psracikan2.close();
                            }
                        }

                if(rsracikan.getString("kd_racik").equals("Puyer")){
                    latin_racikan = "mfla pulv dtd";
                }else if(rsracikan.getString("kd_racik").equals("Capsul")){
                    latin_racikan = "mfla pulv da in cap";
                }else if(rsracikan.getString("kd_racik").equals("Cream")){
                    latin_racikan = "mfla cream da in pot";
                }else if(rsracikan.getString("kd_racik").equals("Gel")){
                    latin_racikan = "mfla gel da in pot";
                }

                Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                    "0",namaobat1,rsracikan.getString("aturan_pakai"),"","",rsracikan.getString("keterangan"),"No. "+intToRoman(rsracikan.getInt("jml")),latin_racikan,"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
                });

                }

                } catch (Exception e) {
                    System.out.println("Notif Resep Detail Racikan = "+e);
                } finally{
                    if(rsracikan!=null){
                        rsracikan.close();
                    }
                    if(psracikan!=null){
                        psracikan.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif Cetak E-Resep = "+e);
            }

            Map<String, Object> param = new HashMap<>();  
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("emailrs",akses.getemailrs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("penanggung",Sequel.cariIsi("select png_jawab from penjab where kd_pj=?",Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),3).toString())));               
            param.put("propinsirs",akses.getpropinsirs());
            param.put("tanggal",Valid.SetTgl3(tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),1).toString()+""));
            param.put("norawat",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),3).toString());
            param.put("pasien",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),5).toString());
            param.put("norm",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),4).toString());
            param.put("peresep",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),6).toString());
            param.put("noresep",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),0).toString());
            bb=Sequel.cariIsi("select berat from pemeriksaan_ralan where no_rawat=?",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),3).toString());
            if(bb.equalsIgnoreCase("")){
                param.put("bb","-");
            }else{
                param.put("bb",bb);
            }
            alergi=Sequel.cariIsi("select alergi from pemeriksaan_ralan where no_rawat=?",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),3).toString());
            param.put("alergi",alergi);
            tgl_lahir=Sequel.cariIsi("select tgl_lahir from pasien where no_rkm_medis =?",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),4).toString());
            param.put("tgl_lahir",tgl_lahir);
            finger=Sequel.cariIsi("select sha1(sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),8).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),6).toString()+"\nID "+(finger.equals("")?tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),8).toString():finger)+"\n"+tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),1).toString());  
            param.put("jam",tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),2).toString());
            param.put("logo",Sequel.cariGambar("select logo from setting")); 

            Valid.MyReport("rptEResep.jasper",param,"::[ Lembar Pemberian Obat Resep Dokter]::");
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnCetakEresepMActionPerformed

    private void BtnCetakEresepM1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCetakEresepM1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCetakEresepM1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgDaftarPermintaanResep dialog = new DlgDaftarPermintaanResep(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnCari;
    private javax.swing.JMenuItem BtnCetakEresep;
    private javax.swing.JMenuItem BtnCetakEresep1;
    private javax.swing.JMenuItem BtnCetakEresepM;
    private javax.swing.JMenuItem BtnCetakEresepM1;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnRekap;
    private widget.Button BtnSeek3;
    private widget.Button BtnSeek4;
    private widget.Button BtnSeek5;
    private widget.Button BtnSeek6;
    private widget.Button BtnTambah;
    private widget.TextBox CrDokter;
    private widget.TextBox CrDokter2;
    private widget.TextBox CrPoli;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextBox Kamar;
    private widget.Label LCount;
    private widget.TextBox TCari;
    private javax.swing.JTabbedPane TabPilihRawat;
    private javax.swing.JTabbedPane TabRawatInap;
    private javax.swing.JTabbedPane TabRawatJalan;
    private widget.ComboBox cmbStatus;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.Label jLabel12;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private widget.Label label10;
    private widget.Label label9;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.panelisi panelisi1;
    private widget.panelisi panelisi2;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane4;
    private widget.ScrollPane scrollPane5;
    private widget.ScrollPane scrollPane6;
    private widget.Table tbDetailPermintaanStok;
    private widget.Table tbDetailResepRalan;
    private widget.Table tbDetailResepRanap;
    private widget.Table tbPermintaanStok;
    private widget.Table tbResepRalan;
    private widget.Table tbResepRanap;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{  
            semua=CrDokter.getText().trim().equals("")&&CrPoli.getText().trim().equals("")&&TCari.getText().trim().equals("");
            ps=koneksi.prepareStatement("select resep_obat.no_resep,resep_obat.tgl_peresepan,resep_obat.jam_peresepan,"+
                    " resep_obat.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,resep_obat.kd_dokter,dokter.nm_dokter, "+
                    " if(resep_obat.jam_peresepan=resep_obat.jam,'Belum Terlayani','Sudah Terlayani') as status,poliklinik.nm_poli,  "+
                    " reg_periksa.kd_poli,penjab.png_jawab from resep_obat "+
                    " inner join reg_periksa on resep_obat.no_rawat=reg_periksa.no_rawat "+
                    " inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    " inner join dokter on resep_obat.kd_dokter=dokter.kd_dokter "+
                    " inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    " inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    " where resep_obat.status='ralan' and resep_obat.tgl_perawatan between ? and ? "+
                    (semua?"":"and dokter.nm_dokter like ? and poliklinik.nm_poli like ? and "+
                    "(resep_obat.no_resep like ? or resep_obat.no_rawat like ? or "+
                    "pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "+
                    "dokter.nm_dokter like ? or penjab.png_jawab like ?)")+" order by resep_obat.tgl_perawatan desc,resep_obat.jam desc");
            try{
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!semua){
                    ps.setString(3,"%"+CrDokter.getText().trim()+"%");
                    ps.setString(4,"%"+CrPoli.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                } 
                rs=ps.executeQuery();
                if(cmbStatus.getSelectedItem().toString().equals("Semua")){
                    while(rs.next()){
                        tabMode.addRow(new String[]{
                            rs.getString("no_resep"),rs.getString("tgl_peresepan"),rs.getString("jam_peresepan"),rs.getString("no_rawat"),
                            rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("nm_dokter"),rs.getString("status"),
                            rs.getString("kd_dokter"),rs.getString("nm_poli"),rs.getString("kd_poli"),rs.getString("png_jawab")
                        });              
                    }  
                }else{
                    while(rs.next()){
                        if(rs.getString("status").equals(cmbStatus.getSelectedItem().toString())){
                            tabMode.addRow(new String[]{
                                rs.getString("no_resep"),rs.getString("tgl_peresepan"),rs.getString("jam_peresepan"),rs.getString("no_rawat"),
                                rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("nm_dokter"),rs.getString("status"),
                                rs.getString("kd_dokter"),rs.getString("nm_poli"),rs.getString("kd_poli"),rs.getString("png_jawab")
                            });
                        }                    
                    }  
                }              
                
                LCount.setText(""+tabMode.getRowCount());
            } catch(Exception ex){
                System.out.println("Notifikasi : "+ex);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }                
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }        
    }

    public void emptTeks() {
        TCari.setText("");
        TCari.requestFocus();
    }

    private void getData() {
        if(tbResepRalan.getSelectedRow()!= -1){
            NoResep=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),0).toString();
            TglPeresepan=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),1).toString();
            JamPeresepan=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),2).toString();
            NoRawat=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),3).toString();
            NoRM=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),4).toString();
            Pasien=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),5).toString();
            DokterPeresep=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),6).toString();
            Status=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),7).toString();
            KodeDokter=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),8).toString();
            Ruang=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),9).toString();
            KodeRuang=tbResepRalan.getValueAt(tbResepRalan.getSelectedRow(),10).toString();
        }
    }
    
    private void getData2() {
        if(tbResepRanap.getSelectedRow()!= -1){
            NoResep=tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),0).toString();
            TglPeresepan=tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),1).toString();
            JamPeresepan=tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),2).toString();
            NoRawat=tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),3).toString();
            NoRM=tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),4).toString();
            Pasien=tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),5).toString();
            DokterPeresep=tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),6).toString();
            Status=tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),7).toString();
            KodeDokter=tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),8).toString();
            Ruang=tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),9).toString();
            KodeRuang=tbResepRanap.getValueAt(tbResepRanap.getSelectedRow(),10).toString();
        }
    }

    private void getData3() {
        if(tbPermintaanStok.getSelectedRow()!= -1){
            NoResep=tbPermintaanStok.getValueAt(tbPermintaanStok.getSelectedRow(),0).toString();
            TglPeresepan=tbPermintaanStok.getValueAt(tbPermintaanStok.getSelectedRow(),1).toString();
            JamPeresepan=tbPermintaanStok.getValueAt(tbPermintaanStok.getSelectedRow(),2).toString();
            NoRawat=tbPermintaanStok.getValueAt(tbPermintaanStok.getSelectedRow(),3).toString();
            NoRM=tbPermintaanStok.getValueAt(tbPermintaanStok.getSelectedRow(),4).toString();
            Pasien=tbPermintaanStok.getValueAt(tbPermintaanStok.getSelectedRow(),5).toString();
            DokterPeresep=tbPermintaanStok.getValueAt(tbPermintaanStok.getSelectedRow(),6).toString();
            Status=tbPermintaanStok.getValueAt(tbPermintaanStok.getSelectedRow(),7).toString();
            KodeDokter=tbPermintaanStok.getValueAt(tbPermintaanStok.getSelectedRow(),8).toString();
            Ruang=tbPermintaanStok.getValueAt(tbPermintaanStok.getSelectedRow(),9).toString();
            KodeRuang=tbPermintaanStok.getValueAt(tbPermintaanStok.getSelectedRow(),10).toString();
        }
    }
    
    public JTable getTable(){
        return tbResepRalan;
    }
    
    public void isCek(){
        BtnEdit.setEnabled(akses.getresep_dokter());
        BtnPrint.setEnabled(akses.getresep_dokter());
        BtnRekap.setEnabled(akses.getresep_obat());
    }
    
    public void setCari(String cari){
        TCari.setText(cari);
    }

    private void tampil2() {
        Valid.tabelKosong(tabMode2);
        try{  
            semua=CrDokter.getText().trim().equals("")&&CrPoli.getText().trim().equals("")&&TCari.getText().trim().equals("");
            ps=koneksi.prepareStatement("select resep_obat.no_resep,resep_obat.tgl_perawatan,resep_obat.jam,resep_obat.no_rawat,pasien.no_rkm_medis,"+
                    " pasien.nm_pasien,resep_obat.kd_dokter,dokter.nm_dokter,if(resep_obat.jam_peresepan=resep_obat.jam,'Belum Terlayani','Sudah Terlayani') as status,"+
                    " poliklinik.nm_poli,resep_obat.status as status_asal,penjab.png_jawab from resep_obat "+
                    " inner join reg_periksa on resep_obat.no_rawat=reg_periksa.no_rawat "+
                    " inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    " inner join dokter on resep_obat.kd_dokter=dokter.kd_dokter "+
                    " inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    " inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    " where resep_obat.status='ralan' and resep_obat.tgl_perawatan between ? and ? "+
                    (semua?"":"and dokter.nm_dokter like ? and poliklinik.nm_poli like ? and "+
                    "(resep_obat.no_resep like ? or resep_obat.no_rawat like ? or "+
                    "pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "+
                    "dokter.nm_dokter like ? or penjab.png_jawab like ?) ")+"order by resep_obat.tgl_perawatan desc,resep_obat.jam desc");
            try{
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!semua){
                    ps.setString(3,"%"+CrDokter.getText().trim()+"%");
                    ps.setString(4,"%"+CrPoli.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                }
                
                rs=ps.executeQuery();
                i=0;
                if(cmbStatus.getSelectedItem().toString().equals("Semua")){
                    while(rs.next()){
                        tabMode2.addRow(new String[]{
                            rs.getString("no_resep"),rs.getString("tgl_perawatan")+" "+rs.getString("jam"),
                            rs.getString("nm_poli"),rs.getString("status"),
                            rs.getString("no_rawat")+" "+rs.getString("no_rkm_medis")+" "+rs.getString("nm_pasien")+" ("+rs.getString("png_jawab")+")",
                            rs.getString("nm_dokter")
                        });
                        tabMode2.addRow(new String[]{"","","Jumlah","Kode Obat","Nama Obat","Aturan Pakai"});                
                        ps2=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter.jml,"+
                            "databarang.kode_sat,resep_dokter.aturan_pakai from resep_dokter inner join databarang on "+
                            "resep_dokter.kode_brng=databarang.kode_brng where resep_dokter.no_resep=? order by databarang.kode_brng");
                        try {
                            ps2.setString(1,rs.getString("no_resep"));
                            rs2=ps2.executeQuery();
                            while(rs2.next()){
                                tabMode2.addRow(new String[]{
                                    "","",rs2.getString("jml")+" "+rs2.getString("kode_sat"),rs2.getString("kode_brng"),rs2.getString("nama_brng"),rs2.getString("aturan_pakai")
                                });
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi 2 : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                            if(ps2!=null){
                                ps2.close();
                            }
                        }
                        ps2=koneksi.prepareStatement(
                                "select resep_dokter_racikan.no_racik,resep_dokter_racikan.nama_racik,"+
                                "resep_dokter_racikan.kd_racik,metode_racik.nm_racik as metode,"+
                                "resep_dokter_racikan.jml_dr,resep_dokter_racikan.aturan_pakai,"+
                                "resep_dokter_racikan.keterangan from resep_dokter_racikan inner join metode_racik "+
                                "on resep_dokter_racikan.kd_racik=metode_racik.kd_racik where "+
                                "resep_dokter_racikan.no_resep=? ");
                        try {
                            ps2.setString(1,rs.getString("no_resep"));
                            rs2=ps2.executeQuery();
                            while(rs2.next()){
                                tabMode2.addRow(new String[]{
                                    "","",rs2.getString("jml_dr")+" "+rs2.getString("metode"),"No.Racik : "+rs2.getString("no_racik"),rs2.getString("nama_racik"),rs2.getString("aturan_pakai")
                                });
                                ps3=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter_racikan_detail.jml,"+
                                    "databarang.kode_sat from resep_dokter_racikan_detail inner join databarang on resep_dokter_racikan_detail.kode_brng=databarang.kode_brng "+
                                    "where resep_dokter_racikan_detail.no_resep=? and resep_dokter_racikan_detail.no_racik=? order by databarang.kode_brng");
                                try {
                                    ps3.setString(1,rs.getString("no_resep"));
                                    ps3.setString(2,rs2.getString("no_racik"));
                                    rs3=ps3.executeQuery();
                                    while(rs3.next()){
                                        tabMode2.addRow(new String[]{
                                            "","","   "+rs3.getString("jml")+" "+rs3.getString("kode_sat"),"   "+rs3.getString("kode_brng"),"   "+rs3.getString("nama_brng"),""
                                        });
                                    }
                                } catch (Exception e) {
                                    System.out.println("Notifikasi 3 : "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                    if(ps3!=null){
                                        ps3.close();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi 2 : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                            if(ps2!=null){
                                ps2.close();
                            }
                        }
                        i++;
                    }
                }else{
                    while(rs.next()){
                        if(rs.getString("status").equals(cmbStatus.getSelectedItem().toString())){
                            tabMode2.addRow(new String[]{
                                rs.getString("no_resep"),rs.getString("tgl_perawatan")+" "+rs.getString("jam"),
                                rs.getString("nm_poli"),rs.getString("status"),
                                rs.getString("no_rawat")+" "+rs.getString("no_rkm_medis")+" "+rs.getString("nm_pasien")+" ("+rs.getString("png_jawab")+")",
                                rs.getString("nm_dokter")
                            });
                            tabMode2.addRow(new String[]{"","","Jumlah","Kode Obat","Nama Obat","Aturan Pakai"});                
                            ps2=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter.jml,"+
                                "databarang.kode_sat,resep_dokter.aturan_pakai from resep_dokter inner join databarang on "+
                                "resep_dokter.kode_brng=databarang.kode_brng where resep_dokter.no_resep=? order by databarang.kode_brng");
                            try {
                                ps2.setString(1,rs.getString("no_resep"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    tabMode2.addRow(new String[]{
                                        "","",rs2.getString("jml")+" "+rs2.getString("kode_sat"),rs2.getString("kode_brng"),rs2.getString("nama_brng"),rs2.getString("aturan_pakai")
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi 2 : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
                            ps2=koneksi.prepareStatement(
                                    "select resep_dokter_racikan.no_racik,resep_dokter_racikan.nama_racik,"+
                                    "resep_dokter_racikan.kd_racik,metode_racik.nm_racik as metode,"+
                                    "resep_dokter_racikan.jml_dr,resep_dokter_racikan.aturan_pakai,"+
                                    "resep_dokter_racikan.keterangan from resep_dokter_racikan inner join metode_racik "+
                                    "on resep_dokter_racikan.kd_racik=metode_racik.kd_racik where "+
                                    "resep_dokter_racikan.no_resep=? ");
                            try {
                                ps2.setString(1,rs.getString("no_resep"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    tabMode2.addRow(new String[]{
                                        "","",rs2.getString("jml_dr")+" "+rs2.getString("metode"),"No.Racik : "+rs2.getString("no_racik"),rs2.getString("nama_racik"),rs2.getString("aturan_pakai")
                                    });
                                    ps3=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter_racikan_detail.jml,"+
                                        "databarang.kode_sat from resep_dokter_racikan_detail inner join databarang on resep_dokter_racikan_detail.kode_brng=databarang.kode_brng "+
                                        "where resep_dokter_racikan_detail.no_resep=? and resep_dokter_racikan_detail.no_racik=? order by databarang.kode_brng");
                                    try {
                                        ps3.setString(1,rs.getString("no_resep"));
                                        ps3.setString(2,rs2.getString("no_racik"));
                                        rs3=ps3.executeQuery();
                                        while(rs3.next()){
                                            tabMode2.addRow(new String[]{
                                                "","","   "+rs3.getString("jml")+" "+rs3.getString("kode_sat"),"   "+rs3.getString("kode_brng"),"   "+rs3.getString("nama_brng"),""
                                            });
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Notifikasi 3 : "+e);
                                    } finally{
                                        if(rs3!=null){
                                            rs3.close();
                                        }
                                        if(ps3!=null){
                                            ps3.close();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi 2 : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
                            i++;
                        }
                    }
                }
                LCount.setText(""+i);
            } catch(Exception ex){
                System.out.println("Notifikasi : "+ex);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }                
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }  
    }

    private void panggilform() {
        dlgobt.setNoRm(NoRawat,NoRM,Pasien,TglPeresepan,JamPeresepan);
        dlgobt.isCek();
        dlgobt.tampilobat2(NoResep);
        dlgobt.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dlgobt.setLocationRelativeTo(internalFrame1);
        TeksKosong();
        dlgobt.setVisible(true);   
    }
    
    private void TeksKosong(){
        NoResep="";
        TglPeresepan="";
        JamPeresepan="";
        NoRawat="";
        NoRM="";
        Pasien="";
        DokterPeresep="";
        Status="";
        KodeDokter="";
        Ruang="";
        KodeRuang="";
    }
    
    private void panggilform2() {
        kamar=KodeRuang;
        bangsal=Sequel.cariIsi("select kd_depo from set_depo_ranap where kd_bangsal=?",kamar);
        if(bangsal.equals("")){
            if(Sequel.cariIsi("select asal_stok from set_lokasi").equals("Gunakan Stok Bangsal")){
                akses.setkdbangsal(kamar);
            }else{
                akses.setkdbangsal(Sequel.cariIsi("select kd_bangsal from set_lokasi"));
            }
        }else{
            akses.setkdbangsal(bangsal);
        }
        dlgobt2.setNoRm(NoRawat,NoRM,Pasien,Valid.SetTgl2(TglPeresepan));
        dlgobt2.isCek();
        dlgobt2.tampilobat2(NoResep);
        dlgobt2.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dlgobt2.setLocationRelativeTo(internalFrame1);
        TeksKosong();
        dlgobt2.setVisible(true);         
    }
    
    private void panggilform3() {
        kamar=KodeRuang;
        bangsal=Sequel.cariIsi("select kd_depo from set_depo_ranap where kd_bangsal=?",kamar);
        if(bangsal.equals("")){
            if(Sequel.cariIsi("select asal_stok from set_lokasi").equals("Gunakan Stok Bangsal")){
                akses.setkdbangsal(kamar);
            }else{
                akses.setkdbangsal(Sequel.cariIsi("select kd_bangsal from set_lokasi"));
            }
        }else{
            akses.setkdbangsal(bangsal);
        }
        dlgstok.setNoRm(NoRawat,NoRM+" "+Pasien);
        dlgstok.isCek();
        dlgstok.tampil2(NoResep);
        dlgstok.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dlgstok.setLocationRelativeTo(internalFrame1);
        TeksKosong();
        dlgstok.setVisible(true);         
    }
    
    public void pilihTab(){
        if(TabPilihRawat.getSelectedIndex()==0){
            pilihRalan();
        }else if(TabPilihRawat.getSelectedIndex()==1){
            pilihRanap();
        }
    }
    
    public void pilihRalan(){
        if(TabRawatJalan.getSelectedIndex()==0){
            tampil();
        }else if(TabRawatJalan.getSelectedIndex()==1){
            tampil2();
        }
    }
    
    public void pilihRanap(){
        if(TabRawatInap.getSelectedIndex()==0){
            tampil3();
        }else if(TabRawatInap.getSelectedIndex()==1){
            tampil4();
        }else if(TabRawatInap.getSelectedIndex()==2){
            tampil5();
        }else if(TabRawatInap.getSelectedIndex()==3){
            tampil6();
        }
    }
    
    public void tampil3() {
        Valid.tabelKosong(tabMode3);
        try{  
            semua=CrDokter2.getText().trim().equals("")&&Kamar.getText().trim().equals("")&&TCari.getText().trim().equals("");
            ps=koneksi.prepareStatement("select resep_obat.no_resep,resep_obat.tgl_peresepan,resep_obat.jam_peresepan,"+
                    " resep_obat.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,resep_obat.kd_dokter,dokter.nm_dokter, "+
                    " if(resep_obat.jam_peresepan=resep_obat.jam,'Belum Terlayani','Sudah Terlayani') as status,"+
                    " bangsal.nm_bangsal,kamar.kd_bangsal,penjab.png_jawab from resep_obat  "+
                    " inner join reg_periksa on resep_obat.no_rawat=reg_periksa.no_rawat "+
                    " inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    " inner join dokter on resep_obat.kd_dokter=dokter.kd_dokter "+
                    " inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    " inner join kamar_inap on reg_periksa.no_rawat=kamar_inap.no_rawat "+
                    " inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar "+
                    " inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
                    " where kamar_inap.stts_pulang='-' and resep_obat.status='ranap' and resep_obat.tgl_perawatan between ? and ? "+
                    (semua?"":"and dokter.nm_dokter like ? and bangsal.nm_bangsal like ? and "+
                    "(resep_obat.no_resep like ? or resep_obat.no_rawat like ? or "+
                    "pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or dokter.nm_dokter like ? or penjab.png_jawab like ?)")+
                    " group by resep_obat.no_resep order by resep_obat.tgl_perawatan desc,resep_obat.jam desc");
            try{
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!semua){
                    ps.setString(3,"%"+CrDokter2.getText().trim()+"%");
                    ps.setString(4,"%"+Kamar.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                }
                
                rs=ps.executeQuery();
                if(cmbStatus.getSelectedItem().toString().equals("Semua")){
                    while(rs.next()){
                        tabMode3.addRow(new String[]{
                            rs.getString("no_resep"),rs.getString("tgl_peresepan"),rs.getString("jam_peresepan"),rs.getString("no_rawat"),
                            rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("nm_dokter"),rs.getString("status"),
                            rs.getString("kd_dokter"),rs.getString("nm_bangsal"),rs.getString("kd_bangsal"),rs.getString("png_jawab")
                        });            
                    } 
                }else{
                    while(rs.next()){
                        if(rs.getString("status").equals(cmbStatus.getSelectedItem().toString())){
                            tabMode3.addRow(new String[]{
                                rs.getString("no_resep"),rs.getString("tgl_peresepan"),rs.getString("jam_peresepan"),rs.getString("no_rawat"),
                                rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("nm_dokter"),rs.getString("status"),
                                rs.getString("kd_dokter"),rs.getString("nm_bangsal"),rs.getString("kd_bangsal"),rs.getString("png_jawab")
                            });  
                        }                  
                    } 
                }               
                
                LCount.setText(""+tabMode3.getRowCount());
            } catch(Exception ex){
                System.out.println("Notifikasi : "+ex);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }      
            
            ps=koneksi.prepareStatement("select resep_obat.no_resep,resep_obat.tgl_peresepan,resep_obat.jam_peresepan,"+
                    " resep_obat.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,resep_obat.kd_dokter,dokter.nm_dokter, "+
                    " if(resep_obat.jam_peresepan=resep_obat.jam,'Belum Terlayani','Sudah Terlayani') as status,"+
                    " bangsal.nm_bangsal,kamar.kd_bangsal,penjab.png_jawab from resep_obat  "+
                    " inner join ranap_gabung on ranap_gabung.no_rawat2=resep_obat.no_rawat "+
                    " inner join reg_periksa on resep_obat.no_rawat=reg_periksa.no_rawat "+
                    " inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    " inner join dokter on resep_obat.kd_dokter=dokter.kd_dokter "+
                    " inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    " inner join kamar_inap on ranap_gabung.no_rawat=kamar_inap.no_rawat "+
                    " inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar "+
                    " inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
                    " where kamar_inap.stts_pulang='-' and resep_obat.status='ranap' and resep_obat.tgl_perawatan between ? and ? "+
                    (semua?"":"and dokter.nm_dokter like ? and bangsal.nm_bangsal like ? and "+
                    "(resep_obat.no_resep like ? or resep_obat.no_rawat like ? or "+
                    "pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or dokter.nm_dokter like ? or penjab.png_jawab like ?)")+
                    " group by resep_obat.no_resep order by resep_obat.tgl_perawatan desc,resep_obat.jam desc");
            try{
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!semua){
                    ps.setString(3,"%"+CrDokter2.getText().trim()+"%");
                    ps.setString(4,"%"+Kamar.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                }
                
                rs=ps.executeQuery();
                if(cmbStatus.getSelectedItem().toString().equals("Semua")){
                    while(rs.next()){
                        tabMode3.addRow(new String[]{
                            rs.getString("no_resep"),rs.getString("tgl_peresepan"),rs.getString("jam_peresepan"),rs.getString("no_rawat"),
                            rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("nm_dokter"),rs.getString("status"),
                            rs.getString("kd_dokter"),rs.getString("nm_bangsal"),rs.getString("kd_bangsal"),rs.getString("png_jawab")
                        });            
                    } 
                }else{
                    while(rs.next()){
                        if(rs.getString("status").equals(cmbStatus.getSelectedItem().toString())){
                            tabMode3.addRow(new String[]{
                                rs.getString("no_resep"),rs.getString("tgl_peresepan"),rs.getString("jam_peresepan"),rs.getString("no_rawat"),
                                rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("nm_dokter"),rs.getString("status"),
                                rs.getString("kd_dokter"),rs.getString("nm_bangsal"),rs.getString("kd_bangsal"),rs.getString("png_jawab")
                            });  
                        }                  
                    } 
                }               
                
                LCount.setText(""+tabMode3.getRowCount());
            } catch(Exception ex){
                System.out.println("Notifikasi : "+ex);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }  
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }        
    }
    
    public void tampil4() {
        Valid.tabelKosong(tabMode4);
        try{  
            semua=CrDokter2.getText().trim().equals("")&&Kamar.getText().trim().equals("")&&TCari.getText().trim().equals("");
            ps=koneksi.prepareStatement("select resep_obat.no_resep,resep_obat.tgl_perawatan,resep_obat.jam,resep_obat.no_rawat,pasien.no_rkm_medis,"+
                    " pasien.nm_pasien,resep_obat.kd_dokter,dokter.nm_dokter,if(resep_obat.jam_peresepan=resep_obat.jam,'Belum Terlayani','Sudah Terlayani') as status,"+
                    " bangsal.nm_bangsal,resep_obat.status as status_asal,penjab.png_jawab from resep_obat "+
                    " inner join reg_periksa on resep_obat.no_rawat=reg_periksa.no_rawat "+
                    " inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    " inner join dokter on resep_obat.kd_dokter=dokter.kd_dokter "+
                    " inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    " inner join kamar_inap on reg_periksa.no_rawat=kamar_inap.no_rawat "+
                    " inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar "+
                    " inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
                    " where kamar_inap.stts_pulang='-' and resep_obat.status='ranap' and resep_obat.tgl_perawatan between ? and ? "+
                    (semua?"":"and dokter.nm_dokter like ? and bangsal.nm_bangsal like ? and "+
                    "(resep_obat.no_resep like ? or resep_obat.no_rawat like ? or "+
                    "pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or dokter.nm_dokter like ? or penjab.png_jawab like ?)")+
                    " group by resep_obat.no_resep order by resep_obat.tgl_perawatan desc,resep_obat.jam desc");
            try{
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!semua){
                    ps.setString(3,"%"+CrDokter2.getText().trim()+"%");
                    ps.setString(4,"%"+Kamar.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                i=0;
                if(cmbStatus.getSelectedItem().toString().equals("Semua")){
                    while(rs.next()){
                        tabMode4.addRow(new String[]{
                            rs.getString("no_resep"),rs.getString("tgl_perawatan")+" "+rs.getString("jam"),
                            rs.getString("nm_bangsal"),rs.getString("status"),
                            rs.getString("no_rawat")+" "+rs.getString("no_rkm_medis")+" "+rs.getString("nm_pasien")+" ("+rs.getString("png_jawab")+")",
                            rs.getString("nm_dokter")
                        });
                        tabMode4.addRow(new String[]{"","","Jumlah","Kode Obat","Nama Obat","Aturan Pakai"});                
                        ps2=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter.jml,"+
                            "databarang.kode_sat,resep_dokter.aturan_pakai from resep_dokter inner join databarang on "+
                            "resep_dokter.kode_brng=databarang.kode_brng where resep_dokter.no_resep=? order by databarang.kode_brng");
                        try {
                            ps2.setString(1,rs.getString("no_resep"));
                            rs2=ps2.executeQuery();
                            while(rs2.next()){
                                tabMode4.addRow(new String[]{
                                    "","",rs2.getString("jml")+" "+rs2.getString("kode_sat"),rs2.getString("kode_brng"),rs2.getString("nama_brng"),rs2.getString("aturan_pakai")
                                });
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi 2 : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                            if(ps2!=null){
                                ps2.close();
                            }
                        }
                        ps2=koneksi.prepareStatement(
                                "select resep_dokter_racikan.no_racik,resep_dokter_racikan.nama_racik,"+
                                "resep_dokter_racikan.kd_racik,metode_racik.nm_racik as metode,"+
                                "resep_dokter_racikan.jml_dr,resep_dokter_racikan.aturan_pakai,"+
                                "resep_dokter_racikan.keterangan from resep_dokter_racikan inner join metode_racik "+
                                "on resep_dokter_racikan.kd_racik=metode_racik.kd_racik where "+
                                "resep_dokter_racikan.no_resep=? ");
                        try {
                            ps2.setString(1,rs.getString("no_resep"));
                            rs2=ps2.executeQuery();
                            while(rs2.next()){
                                tabMode4.addRow(new String[]{
                                    "","",rs2.getString("jml_dr")+" "+rs2.getString("metode"),"No.Racik : "+rs2.getString("no_racik"),rs2.getString("nama_racik"),rs2.getString("aturan_pakai")
                                });
                                ps3=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter_racikan_detail.jml,"+
                                    "databarang.kode_sat from resep_dokter_racikan_detail inner join databarang on resep_dokter_racikan_detail.kode_brng=databarang.kode_brng "+
                                    "where resep_dokter_racikan_detail.no_resep=? and resep_dokter_racikan_detail.no_racik=? order by databarang.kode_brng");
                                try {
                                    ps3.setString(1,rs.getString("no_resep"));
                                    ps3.setString(2,rs2.getString("no_racik"));
                                    rs3=ps3.executeQuery();
                                    while(rs3.next()){
                                        tabMode4.addRow(new String[]{
                                            "","","   "+rs3.getString("jml")+" "+rs3.getString("kode_sat"),"   "+rs3.getString("kode_brng"),"   "+rs3.getString("nama_brng"),""
                                        });
                                    }
                                } catch (Exception e) {
                                    System.out.println("Notifikasi 3 : "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                    if(ps3!=null){
                                        ps3.close();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi 2 : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                            if(ps2!=null){
                                ps2.close();
                            }
                        }
                        i++;
                    }
                }else{
                    while(rs.next()){
                        if(rs.getString("status").equals(cmbStatus.getSelectedItem().toString())){
                            tabMode4.addRow(new String[]{
                                rs.getString("no_resep"),rs.getString("tgl_perawatan")+" "+rs.getString("jam"),
                                rs.getString("nm_bangsal"),rs.getString("status"),
                                rs.getString("no_rawat")+" "+rs.getString("no_rkm_medis")+" "+rs.getString("nm_pasien")+" ("+rs.getString("png_jawab")+")",
                                rs.getString("nm_dokter")
                            });
                            tabMode4.addRow(new String[]{"","","Jumlah","Kode Obat","Nama Obat","Aturan Pakai"});                
                            ps2=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter.jml,"+
                                "databarang.kode_sat,resep_dokter.aturan_pakai from resep_dokter inner join databarang on "+
                                "resep_dokter.kode_brng=databarang.kode_brng where resep_dokter.no_resep=? order by databarang.kode_brng");
                            try {
                                ps2.setString(1,rs.getString("no_resep"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    tabMode4.addRow(new String[]{
                                        "","",rs2.getString("jml")+" "+rs2.getString("kode_sat"),rs2.getString("kode_brng"),rs2.getString("nama_brng"),rs2.getString("aturan_pakai")
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi 2 : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
                            ps2=koneksi.prepareStatement(
                                    "select resep_dokter_racikan.no_racik,resep_dokter_racikan.nama_racik,"+
                                    "resep_dokter_racikan.kd_racik,metode_racik.nm_racik as metode,"+
                                    "resep_dokter_racikan.jml_dr,resep_dokter_racikan.aturan_pakai,"+
                                    "resep_dokter_racikan.keterangan from resep_dokter_racikan inner join metode_racik "+
                                    "on resep_dokter_racikan.kd_racik=metode_racik.kd_racik where "+
                                    "resep_dokter_racikan.no_resep=? ");
                            try {
                                ps2.setString(1,rs.getString("no_resep"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    tabMode4.addRow(new String[]{
                                        "","",rs2.getString("jml_dr")+" "+rs2.getString("metode"),"No.Racik : "+rs2.getString("no_racik"),rs2.getString("nama_racik"),rs2.getString("aturan_pakai")
                                    });
                                    ps3=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter_racikan_detail.jml,"+
                                        "databarang.kode_sat from resep_dokter_racikan_detail inner join databarang on resep_dokter_racikan_detail.kode_brng=databarang.kode_brng "+
                                        "where resep_dokter_racikan_detail.no_resep=? and resep_dokter_racikan_detail.no_racik=? order by databarang.kode_brng");
                                    try {
                                        ps3.setString(1,rs.getString("no_resep"));
                                        ps3.setString(2,rs2.getString("no_racik"));
                                        rs3=ps3.executeQuery();
                                        while(rs3.next()){
                                            tabMode4.addRow(new String[]{
                                                "","","   "+rs3.getString("jml")+" "+rs3.getString("kode_sat"),"   "+rs3.getString("kode_brng"),"   "+rs3.getString("nama_brng"),""
                                            });
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Notifikasi 3 : "+e);
                                    } finally{
                                        if(rs3!=null){
                                            rs3.close();
                                        }
                                        if(ps3!=null){
                                            ps3.close();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi 2 : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
                            i++;
                        }
                    }
                }
                    
                LCount.setText(""+i++);
            } catch(Exception ex){
                System.out.println("Notifikasi : "+ex);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            } 
            
            ps=koneksi.prepareStatement("select resep_obat.no_resep,resep_obat.tgl_perawatan,resep_obat.jam,resep_obat.no_rawat,pasien.no_rkm_medis,"+
                    " pasien.nm_pasien,resep_obat.kd_dokter,dokter.nm_dokter,if(resep_obat.jam_peresepan=resep_obat.jam,'Belum Terlayani','Sudah Terlayani') as status,"+
                    " bangsal.nm_bangsal,resep_obat.status as status_asal,penjab.png_jawab from resep_obat "+
                    " inner join ranap_gabung on ranap_gabung.no_rawat2=resep_obat.no_rawat "+
                    " inner join reg_periksa on resep_obat.no_rawat=reg_periksa.no_rawat "+
                    " inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    " inner join dokter on resep_obat.kd_dokter=dokter.kd_dokter "+
                    " inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    " inner join kamar_inap on ranap_gabung.no_rawat=kamar_inap.no_rawat "+
                    " inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar "+
                    " inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
                    " where kamar_inap.stts_pulang='-' and resep_obat.status='ranap' and resep_obat.tgl_perawatan between ? and ? "+
                    (semua?"":"and dokter.nm_dokter like ? and bangsal.nm_bangsal like ? and "+
                    "(resep_obat.no_resep like ? or resep_obat.no_rawat like ? or "+
                    "pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or dokter.nm_dokter like ? or penjab.png_jawab like ?)")+
                    " group by resep_obat.no_resep order by resep_obat.tgl_perawatan desc,resep_obat.jam desc");
            try{
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!semua){
                    ps.setString(3,"%"+CrDokter2.getText().trim()+"%");
                    ps.setString(4,"%"+Kamar.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                i=0;
                if(cmbStatus.getSelectedItem().toString().equals("Semua")){
                    while(rs.next()){
                        tabMode4.addRow(new String[]{
                            rs.getString("no_resep"),rs.getString("tgl_perawatan")+" "+rs.getString("jam"),
                            rs.getString("nm_bangsal"),rs.getString("status"),
                            rs.getString("no_rawat")+" "+rs.getString("no_rkm_medis")+" "+rs.getString("nm_pasien")+" ("+rs.getString("png_jawab")+")",
                            rs.getString("nm_dokter")
                        });
                        tabMode4.addRow(new String[]{"","","Jumlah","Kode Obat","Nama Obat","Aturan Pakai"});                
                        ps2=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter.jml,"+
                            "databarang.kode_sat,resep_dokter.aturan_pakai from resep_dokter inner join databarang on "+
                            "resep_dokter.kode_brng=databarang.kode_brng where resep_dokter.no_resep=? order by databarang.kode_brng");
                        try {
                            ps2.setString(1,rs.getString("no_resep"));
                            rs2=ps2.executeQuery();
                            while(rs2.next()){
                                tabMode4.addRow(new String[]{
                                    "","",rs2.getString("jml")+" "+rs2.getString("kode_sat"),rs2.getString("kode_brng"),rs2.getString("nama_brng"),rs2.getString("aturan_pakai")
                                });
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi 2 : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                            if(ps2!=null){
                                ps2.close();
                            }
                        }
                        ps2=koneksi.prepareStatement(
                                "select resep_dokter_racikan.no_racik,resep_dokter_racikan.nama_racik,"+
                                "resep_dokter_racikan.kd_racik,metode_racik.nm_racik as metode,"+
                                "resep_dokter_racikan.jml_dr,resep_dokter_racikan.aturan_pakai,"+
                                "resep_dokter_racikan.keterangan from resep_dokter_racikan inner join metode_racik "+
                                "on resep_dokter_racikan.kd_racik=metode_racik.kd_racik where "+
                                "resep_dokter_racikan.no_resep=? ");
                        try {
                            ps2.setString(1,rs.getString("no_resep"));
                            rs2=ps2.executeQuery();
                            while(rs2.next()){
                                tabMode4.addRow(new String[]{
                                    "","",rs2.getString("jml_dr")+" "+rs2.getString("metode"),"No.Racik : "+rs2.getString("no_racik"),rs2.getString("nama_racik"),rs2.getString("aturan_pakai")
                                });
                                ps3=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter_racikan_detail.jml,"+
                                    "databarang.kode_sat from resep_dokter_racikan_detail inner join databarang on resep_dokter_racikan_detail.kode_brng=databarang.kode_brng "+
                                    "where resep_dokter_racikan_detail.no_resep=? and resep_dokter_racikan_detail.no_racik=? order by databarang.kode_brng");
                                try {
                                    ps3.setString(1,rs.getString("no_resep"));
                                    ps3.setString(2,rs2.getString("no_racik"));
                                    rs3=ps3.executeQuery();
                                    while(rs3.next()){
                                        tabMode4.addRow(new String[]{
                                            "","","   "+rs3.getString("jml")+" "+rs3.getString("kode_sat"),"   "+rs3.getString("kode_brng"),"   "+rs3.getString("nama_brng"),""
                                        });
                                    }
                                } catch (Exception e) {
                                    System.out.println("Notifikasi 3 : "+e);
                                } finally{
                                    if(rs3!=null){
                                        rs3.close();
                                    }
                                    if(ps3!=null){
                                        ps3.close();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi 2 : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                            if(ps2!=null){
                                ps2.close();
                            }
                        }
                        i++;
                    }
                }else{
                    while(rs.next()){
                        if(rs.getString("status").equals(cmbStatus.getSelectedItem().toString())){
                            tabMode4.addRow(new String[]{
                                rs.getString("no_resep"),rs.getString("tgl_perawatan")+" "+rs.getString("jam"),
                                rs.getString("nm_bangsal"),rs.getString("status"),
                                rs.getString("no_rawat")+" "+rs.getString("no_rkm_medis")+" "+rs.getString("nm_pasien")+" ("+rs.getString("png_jawab")+")",
                                rs.getString("nm_dokter")
                            });
                            tabMode4.addRow(new String[]{"","","Jumlah","Kode Obat","Nama Obat","Aturan Pakai"});                
                            ps2=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter.jml,"+
                                "databarang.kode_sat,resep_dokter.aturan_pakai from resep_dokter inner join databarang on "+
                                "resep_dokter.kode_brng=databarang.kode_brng where resep_dokter.no_resep=? order by databarang.kode_brng");
                            try {
                                ps2.setString(1,rs.getString("no_resep"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    tabMode4.addRow(new String[]{
                                        "","",rs2.getString("jml")+" "+rs2.getString("kode_sat"),rs2.getString("kode_brng"),rs2.getString("nama_brng"),rs2.getString("aturan_pakai")
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi 2 : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
                            ps2=koneksi.prepareStatement(
                                    "select resep_dokter_racikan.no_racik,resep_dokter_racikan.nama_racik,"+
                                    "resep_dokter_racikan.kd_racik,metode_racik.nm_racik as metode,"+
                                    "resep_dokter_racikan.jml_dr,resep_dokter_racikan.aturan_pakai,"+
                                    "resep_dokter_racikan.keterangan from resep_dokter_racikan inner join metode_racik "+
                                    "on resep_dokter_racikan.kd_racik=metode_racik.kd_racik where "+
                                    "resep_dokter_racikan.no_resep=? ");
                            try {
                                ps2.setString(1,rs.getString("no_resep"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    tabMode4.addRow(new String[]{
                                        "","",rs2.getString("jml_dr")+" "+rs2.getString("metode"),"No.Racik : "+rs2.getString("no_racik"),rs2.getString("nama_racik"),rs2.getString("aturan_pakai")
                                    });
                                    ps3=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,resep_dokter_racikan_detail.jml,"+
                                        "databarang.kode_sat from resep_dokter_racikan_detail inner join databarang on resep_dokter_racikan_detail.kode_brng=databarang.kode_brng "+
                                        "where resep_dokter_racikan_detail.no_resep=? and resep_dokter_racikan_detail.no_racik=? order by databarang.kode_brng");
                                    try {
                                        ps3.setString(1,rs.getString("no_resep"));
                                        ps3.setString(2,rs2.getString("no_racik"));
                                        rs3=ps3.executeQuery();
                                        while(rs3.next()){
                                            tabMode4.addRow(new String[]{
                                                "","","   "+rs3.getString("jml")+" "+rs3.getString("kode_sat"),"   "+rs3.getString("kode_brng"),"   "+rs3.getString("nama_brng"),""
                                            });
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Notifikasi 3 : "+e);
                                    } finally{
                                        if(rs3!=null){
                                            rs3.close();
                                        }
                                        if(ps3!=null){
                                            ps3.close();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi 2 : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
                            i++;
                        }
                    }
                }
                    
                LCount.setText(""+i++);
            } catch(Exception ex){
                System.out.println("Notifikasi : "+ex);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }  
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }  
    }
    
    public void tampil5() {
        Valid.tabelKosong(tabMode5);
        try{  
            semua=CrDokter2.getText().trim().equals("")&&Kamar.getText().trim().equals("")&&TCari.getText().trim().equals("");
            ps=koneksi.prepareStatement("select permintaan_stok_obat_pasien.no_permintaan,permintaan_stok_obat_pasien.tgl_permintaan,permintaan_stok_obat_pasien.jam,"+
                    " permintaan_stok_obat_pasien.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,permintaan_stok_obat_pasien.kd_dokter,dokter.nm_dokter, "+
                    " if(permintaan_stok_obat_pasien.status='Belum','Belum Terlayani','Sudah Terlayani') as status,"+
                    " bangsal.nm_bangsal,kamar.kd_bangsal,penjab.png_jawab from permintaan_stok_obat_pasien  "+
                    " inner join reg_periksa on permintaan_stok_obat_pasien.no_rawat=reg_periksa.no_rawat "+
                    " inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    " inner join dokter on permintaan_stok_obat_pasien.kd_dokter=dokter.kd_dokter "+
                    " inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    " inner join kamar_inap on reg_periksa.no_rawat=kamar_inap.no_rawat "+
                    " inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar "+
                    " inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
                    " where kamar_inap.stts_pulang='-' and permintaan_stok_obat_pasien.tgl_permintaan between ? and ? "+
                    (semua?"":"and dokter.nm_dokter like ? and bangsal.nm_bangsal like ? and "+
                    "(permintaan_stok_obat_pasien.no_permintaan like ? or permintaan_stok_obat_pasien.no_rawat like ? or "+
                    "pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or dokter.nm_dokter like ? or penjab.png_jawab like ?)")+
                    " group by permintaan_stok_obat_pasien.no_permintaan order by permintaan_stok_obat_pasien.tgl_permintaan desc,permintaan_stok_obat_pasien.jam desc");
            try{
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!semua){
                    ps.setString(3,"%"+CrDokter2.getText().trim()+"%");
                    ps.setString(4,"%"+Kamar.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                }
                
                rs=ps.executeQuery();
                if(cmbStatus.getSelectedItem().toString().equals("Semua")){
                    while(rs.next()){
                        tabMode5.addRow(new String[]{
                            rs.getString("no_permintaan"),rs.getString("tgl_permintaan"),rs.getString("jam"),rs.getString("no_rawat"),
                            rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("nm_dokter"),rs.getString("status"),
                            rs.getString("kd_dokter"),rs.getString("nm_bangsal"),rs.getString("kd_bangsal"),rs.getString("png_jawab")
                        });            
                    } 
                }else{
                    while(rs.next()){
                        if(rs.getString("status").equals(cmbStatus.getSelectedItem().toString())){
                            tabMode5.addRow(new String[]{
                                rs.getString("no_permintaan"),rs.getString("tgl_permintaan"),rs.getString("jam"),rs.getString("no_rawat"),
                                rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("nm_dokter"),rs.getString("status"),
                                rs.getString("kd_dokter"),rs.getString("nm_bangsal"),rs.getString("kd_bangsal"),rs.getString("png_jawab")
                            });  
                        }                  
                    } 
                }               
                
                LCount.setText(""+tabMode5.getRowCount());
            } catch(Exception ex){
                System.out.println("Notifikasi : "+ex);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }      
            
            ps=koneksi.prepareStatement("select permintaan_stok_obat_pasien.no_permintaan,permintaan_stok_obat_pasien.tgl_permintaan,permintaan_stok_obat_pasien.jam,"+
                    " permintaan_stok_obat_pasien.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,permintaan_stok_obat_pasien.kd_dokter,dokter.nm_dokter, "+
                    " if(permintaan_stok_obat_pasien.status='Belum','Belum Terlayani','Sudah Terlayani') as status,"+
                    " bangsal.nm_bangsal,kamar.kd_bangsal,penjab.png_jawab from permintaan_stok_obat_pasien  "+
                    " inner join ranap_gabung on ranap_gabung.no_rawat2=permintaan_stok_obat_pasien.no_rawat "+
                    " inner join reg_periksa on permintaan_stok_obat_pasien.no_rawat=reg_periksa.no_rawat "+
                    " inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    " inner join dokter on permintaan_stok_obat_pasien.kd_dokter=dokter.kd_dokter "+
                    " inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    " inner join kamar_inap on ranap_gabung.no_rawat=kamar_inap.no_rawat "+
                    " inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar "+
                    " inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
                    " where kamar_inap.stts_pulang='-' and permintaan_stok_obat_pasien.tgl_permintaan between ? and ? "+
                    (semua?"":"and dokter.nm_dokter like ? and bangsal.nm_bangsal like ? and "+
                    "(permintaan_stok_obat_pasien.no_permintaan like ? or permintaan_stok_obat_pasien.no_rawat like ? or "+
                    "pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or dokter.nm_dokter like ? or penjab.png_jawab like ?)")+
                    " group by permintaan_stok_obat_pasien.no_permintaan order by permintaan_stok_obat_pasien.tgl_permintaan desc,permintaan_stok_obat_pasien.jam desc");
            try{
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!semua){
                    ps.setString(3,"%"+CrDokter2.getText().trim()+"%");
                    ps.setString(4,"%"+Kamar.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                }
                
                rs=ps.executeQuery();
                if(cmbStatus.getSelectedItem().toString().equals("Semua")){
                    while(rs.next()){
                        tabMode5.addRow(new String[]{
                            rs.getString("no_permintaan"),rs.getString("tgl_permintaan"),rs.getString("jam"),rs.getString("no_rawat"),
                            rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("nm_dokter"),rs.getString("status"),
                            rs.getString("kd_dokter"),rs.getString("nm_bangsal"),rs.getString("kd_bangsal"),rs.getString("png_jawab")
                        });            
                    } 
                }else{
                    while(rs.next()){
                        if(rs.getString("status").equals(cmbStatus.getSelectedItem().toString())){
                            tabMode5.addRow(new String[]{
                                rs.getString("no_permintaan"),rs.getString("tgl_permintaan"),rs.getString("jam"),rs.getString("no_rawat"),
                                rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("nm_dokter"),rs.getString("status"),
                                rs.getString("kd_dokter"),rs.getString("nm_bangsal"),rs.getString("kd_bangsal"),rs.getString("png_jawab")
                            });  
                        }                  
                    } 
                }               
                
                LCount.setText(""+tabMode5.getRowCount());
            } catch(Exception ex){
                System.out.println("Notifikasi : "+ex);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }        
    }
    
    public void tampil6() {
        Valid.tabelKosong(tabMode6);
        try{  
            semua=CrDokter2.getText().trim().equals("")&&Kamar.getText().trim().equals("")&&TCari.getText().trim().equals("");
            ps=koneksi.prepareStatement("select permintaan_stok_obat_pasien.no_permintaan,permintaan_stok_obat_pasien.tgl_permintaan,permintaan_stok_obat_pasien.jam,permintaan_stok_obat_pasien.no_rawat,pasien.no_rkm_medis,"+
                    " pasien.nm_pasien,permintaan_stok_obat_pasien.kd_dokter,dokter.nm_dokter,if(permintaan_stok_obat_pasien.status='Belum','Belum Terlayani','Sudah Terlayani') as status,"+
                    " bangsal.nm_bangsal,permintaan_stok_obat_pasien.status as status_asal,penjab.png_jawab from permintaan_stok_obat_pasien "+
                    " inner join reg_periksa on permintaan_stok_obat_pasien.no_rawat=reg_periksa.no_rawat "+
                    " inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    " inner join dokter on permintaan_stok_obat_pasien.kd_dokter=dokter.kd_dokter "+
                    " inner join kamar_inap on reg_periksa.no_rawat=kamar_inap.no_rawat "+
                    " inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar "+
                    " inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
                    " inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    " where kamar_inap.stts_pulang='-' and permintaan_stok_obat_pasien.tgl_permintaan between ? and ? "+
                    (semua?"":"and dokter.nm_dokter like ? and bangsal.nm_bangsal like ? and "+
                    "(permintaan_stok_obat_pasien.no_permintaan like ? or permintaan_stok_obat_pasien.no_rawat like ? or "+
                    "pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or dokter.nm_dokter like ? or penjab.png_jawab like ?)")+
                    " group by permintaan_stok_obat_pasien.no_permintaan order by permintaan_stok_obat_pasien.tgl_permintaan desc,permintaan_stok_obat_pasien.jam desc");
            try{
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!semua){
                    ps.setString(3,"%"+CrDokter2.getText().trim()+"%");
                    ps.setString(4,"%"+Kamar.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                i=0;
                if(cmbStatus.getSelectedItem().toString().equals("Semua")){
                    while(rs.next()){
                        tabMode6.addRow(new String[]{
                            rs.getString("no_permintaan"),rs.getString("tgl_permintaan")+" "+rs.getString("jam"),
                            rs.getString("nm_bangsal"),rs.getString("status"),
                            rs.getString("no_rawat")+" "+rs.getString("no_rkm_medis")+" "+rs.getString("nm_pasien")+" ("+rs.getString("png_jawab")+")",
                            rs.getString("nm_dokter")
                        });
                        tabMode6.addRow(new String[]{"","","Jumlah","Kode Obat","Nama Obat","Aturan Pakai"});                
                        ps2=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,detail_permintaan_stok_obat_pasien.jml,"+
                            "databarang.kode_sat,detail_permintaan_stok_obat_pasien.aturan_pakai,detail_permintaan_stok_obat_pasien.pg,"+
                            "detail_permintaan_stok_obat_pasien.sg,detail_permintaan_stok_obat_pasien.sr,detail_permintaan_stok_obat_pasien.ml "+
                            "from detail_permintaan_stok_obat_pasien inner join databarang on detail_permintaan_stok_obat_pasien.kode_brng=databarang.kode_brng "+
                            "where detail_permintaan_stok_obat_pasien.no_permintaan=? order by databarang.kode_brng");
                        try {
                            ps2.setString(1,rs.getString("no_permintaan"));
                            rs2=ps2.executeQuery();
                            while(rs2.next()){
                                tabMode6.addRow(new String[]{
                                    "","",rs2.getString("jml")+" "+rs2.getString("kode_sat"),rs2.getString("kode_brng"),rs2.getString("nama_brng"),
                                    "Pg : "+rs2.getString("pg").replaceAll("true","✓").replaceAll("false","✕")+"  "+
                                    "Sg : "+rs2.getString("sg").replaceAll("true","✓").replaceAll("false","✕")+"  "+
                                    "Sr : "+rs2.getString("sr").replaceAll("true","✓").replaceAll("false","✕")+"  "+
                                    "Ml : "+rs2.getString("ml").replaceAll("true","✓").replaceAll("false","✕")+"   "+
                                    rs2.getString("aturan_pakai")
                                });
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi 2 : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                            if(ps2!=null){
                                ps2.close();
                            }
                        }
                        i++;
                    }
                }else{
                    while(rs.next()){
                        if(rs.getString("status").equals(cmbStatus.getSelectedItem().toString())){
                            tabMode6.addRow(new String[]{
                                rs.getString("no_permintaan"),rs.getString("tgl_permintaan")+" "+rs.getString("jam"),
                                rs.getString("nm_bangsal"),rs.getString("status"),
                                rs.getString("no_rawat")+" "+rs.getString("no_rkm_medis")+" "+rs.getString("nm_pasien")+" ("+rs.getString("png_jawab")+")",
                                rs.getString("nm_dokter")
                            });
                            tabMode6.addRow(new String[]{"","","Jumlah","Kode Obat","Nama Obat","Aturan Pakai"});                
                            ps2=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,detail_permintaan_stok_obat_pasien.jml,"+
                                "databarang.kode_sat,detail_permintaan_stok_obat_pasien.aturan_pakai from detail_permintaan_stok_obat_pasien inner join databarang on "+
                                "detail_permintaan_stok_obat_pasien.kode_brng=databarang.kode_brng where detail_permintaan_stok_obat_pasien.no_permintaan=? order by databarang.kode_brng");
                            try {
                                ps2.setString(1,rs.getString("no_permintaan"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    tabMode6.addRow(new String[]{
                                        "","",rs2.getString("jml")+" "+rs2.getString("kode_sat"),rs2.getString("kode_brng"),rs2.getString("nama_brng"),rs2.getString("aturan_pakai")
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi 2 : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
                            i++;
                        }
                    }
                }
                    
                LCount.setText(""+i++);
            } catch(Exception ex){
                System.out.println("Notifikasi : "+ex);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }             
            
            ps=koneksi.prepareStatement("select permintaan_stok_obat_pasien.no_permintaan,permintaan_stok_obat_pasien.tgl_permintaan,permintaan_stok_obat_pasien.jam,permintaan_stok_obat_pasien.no_rawat,pasien.no_rkm_medis,"+
                    " pasien.nm_pasien,permintaan_stok_obat_pasien.kd_dokter,dokter.nm_dokter,if(permintaan_stok_obat_pasien.status='Belum','Belum Terlayani','Sudah Terlayani') as status,"+
                    " bangsal.nm_bangsal,permintaan_stok_obat_pasien.status as status_asal,penjab.png_jawab from permintaan_stok_obat_pasien "+
                    " inner join ranap_gabung on ranap_gabung.no_rawat2=permintaan_stok_obat_pasien.no_rawat "+
                    " inner join reg_periksa on permintaan_stok_obat_pasien.no_rawat=reg_periksa.no_rawat "+
                    " inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    " inner join dokter on permintaan_stok_obat_pasien.kd_dokter=dokter.kd_dokter "+
                    " inner join kamar_inap on ranap_gabung.no_rawat=kamar_inap.no_rawat "+
                    " inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar "+
                    " inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
                    " inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    " where kamar_inap.stts_pulang='-' and permintaan_stok_obat_pasien.tgl_permintaan between ? and ? "+
                    (semua?"":"and dokter.nm_dokter like ? and bangsal.nm_bangsal like ? and "+
                    "(permintaan_stok_obat_pasien.no_permintaan like ? or permintaan_stok_obat_pasien.no_rawat like ? or "+
                    "pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or dokter.nm_dokter like ? or penjab.png_jawab like ?)")+
                    " group by permintaan_stok_obat_pasien.no_permintaan order by permintaan_stok_obat_pasien.tgl_permintaan desc,permintaan_stok_obat_pasien.jam desc");
            try{
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!semua){
                    ps.setString(3,"%"+CrDokter2.getText().trim()+"%");
                    ps.setString(4,"%"+Kamar.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                }
                rs=ps.executeQuery();
                i=0;
                if(cmbStatus.getSelectedItem().toString().equals("Semua")){
                    while(rs.next()){
                        tabMode6.addRow(new String[]{
                            rs.getString("no_permintaan"),rs.getString("tgl_permintaan")+" "+rs.getString("jam"),
                            rs.getString("nm_bangsal"),rs.getString("status"),
                            rs.getString("no_rawat")+" "+rs.getString("no_rkm_medis")+" "+rs.getString("nm_pasien")+" ("+rs.getString("png_jawab")+")",
                            rs.getString("nm_dokter")
                        });
                        tabMode6.addRow(new String[]{"","","Jumlah","Kode Obat","Nama Obat","Aturan Pakai"});                
                        ps2=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,detail_permintaan_stok_obat_pasien.jml,"+
                            "databarang.kode_sat,detail_permintaan_stok_obat_pasien.aturan_pakai from detail_permintaan_stok_obat_pasien inner join databarang on "+
                            "detail_permintaan_stok_obat_pasien.kode_brng=databarang.kode_brng where detail_permintaan_stok_obat_pasien.no_permintaan=? order by databarang.kode_brng");
                        try {
                            ps2.setString(1,rs.getString("no_permintaan"));
                            rs2=ps2.executeQuery();
                            while(rs2.next()){
                                tabMode6.addRow(new String[]{
                                    "","",rs2.getString("jml")+" "+rs2.getString("kode_sat"),rs2.getString("kode_brng"),rs2.getString("nama_brng"),rs2.getString("aturan_pakai")
                                });
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi 2 : "+e);
                        } finally{
                            if(rs2!=null){
                                rs2.close();
                            }
                            if(ps2!=null){
                                ps2.close();
                            }
                        }
                        i++;
                    }
                }else{
                    while(rs.next()){
                        if(rs.getString("status").equals(cmbStatus.getSelectedItem().toString())){
                            tabMode6.addRow(new String[]{
                                rs.getString("no_permintaan"),rs.getString("tgl_permintaan")+" "+rs.getString("jam"),
                                rs.getString("nm_bangsal"),rs.getString("status"),
                                rs.getString("no_rawat")+" "+rs.getString("no_rkm_medis")+" "+rs.getString("nm_pasien")+" ("+rs.getString("png_jawab")+")",
                                rs.getString("nm_dokter")
                            });
                            tabMode6.addRow(new String[]{"","","Jumlah","Kode Obat","Nama Obat","Aturan Pakai"});                
                            ps2=koneksi.prepareStatement("select databarang.kode_brng,databarang.nama_brng,detail_permintaan_stok_obat_pasien.jml,"+
                                "databarang.kode_sat,detail_permintaan_stok_obat_pasien.aturan_pakai from detail_permintaan_stok_obat_pasien inner join databarang on "+
                                "detail_permintaan_stok_obat_pasien.kode_brng=databarang.kode_brng where detail_permintaan_stok_obat_pasien.no_permintaan=? order by databarang.kode_brng");
                            try {
                                ps2.setString(1,rs.getString("no_permintaan"));
                                rs2=ps2.executeQuery();
                                while(rs2.next()){
                                    tabMode6.addRow(new String[]{
                                        "","",rs2.getString("jml")+" "+rs2.getString("kode_sat"),rs2.getString("kode_brng"),rs2.getString("nama_brng"),rs2.getString("aturan_pakai")
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi 2 : "+e);
                            } finally{
                                if(rs2!=null){
                                    rs2.close();
                                }
                                if(ps2!=null){
                                    ps2.close();
                                }
                            }
                            i++;
                        }
                    }
                }
                    
                LCount.setText(""+i++);
            } catch(Exception ex){
                System.out.println("Notifikasi : "+ex);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }   
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }  
    }
    
    public void setNoRm(String norawat){
        TCari.setText(norawat);
        pilihTab();
    }
    
    private void jam(){
        ActionListener taskPerformer = (ActionEvent e) -> {
            if(aktif==true){
                nol_detik = "";
                Date now = Calendar.getInstance().getTime();
                nilai_detik = now.getSeconds();
                if (nilai_detik <= 9) {
                    nol_detik = "0";
                }

                detik = nol_detik + Integer.toString(nilai_detik);
                if(detik.equals("05")){
                    resepbaru=0;
                    if(formalarm.contains("ralan")){
                        tampil();
                        for(i=0;i<tbResepRalan.getRowCount();i++){
                            if(tbResepRalan.getValueAt(i,7).toString().equals("Belum Terlayani")){
                                resepbaru++;
                            }
                        }
                    }

                    if(formalarm.contains("ranap")){
                        tampil3();
                        for(i=0;i<tbResepRanap.getRowCount();i++){
                            if(tbResepRanap.getValueAt(i,7).toString().equals("Belum Terlayani")){
                                resepbaru++;
                            }
                        }
                        
                        tampil5();
                        for(i=0;i<tbPermintaanStok.getRowCount();i++){
                            if(tbPermintaanStok.getValueAt(i,7).toString().equals("Belum Terlayani")){
                                resepbaru++;
                            }
                        }
                    }

                    if(resepbaru>0){
                        try {
                            music = new BackgroundMusic("./suara/alarm.mp3");
                            music.start();
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }
            }                
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }
}
