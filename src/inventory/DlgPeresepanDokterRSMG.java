/*
  Dilarang keras menggandakan/mengcopy/menyebarkan/membajak/mendecompile 
  Software ini dalam bentuk apapun tanpa seijin pembuat software
  (Khanza.Soft Media). Bagi yang sengaja membajak softaware ini ta
  npa ijin, kami sumpahi sial 1000 turunan, miskin sampai 500 turu
  nan. Selalu mendapat kecelakaan sampai 400 turunan. Anak pertama
  nya cacat tidak punya kaki sampai 300 turunan. Susah cari jodoh
  sampai umur 50 tahun sampai 200 turunan. Ya Alloh maafkan kami 
  karena telah berdoa buruk, semua ini kami lakukan karena kami ti
  dak pernah rela karya kami dibajak tanpa ijin.
 */

package inventory;

import fungsi.WarnaTable2;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import static inventory.DlgResepObat.intToRoman;
import inventory.DlgDaftarPermintaanResep;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import widget.Button;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author dosen
 */
public final class DlgPeresepanDokterRSMG extends javax.swing.JDialog {
    private final DefaultTableModel tabModeResep,tabModeResepRacikan;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private PreparedStatement psresep,pscarikapasitas,psresepasuransi,ps2,ps,psracikan,psracikan2;
    private ResultSet rsobat,carikapasitas,rs2,rs,rsracikan,rsracikan2;
    private double x=0,y=0,kenaikan=0,ttl=0,ppnobat=0,jumlahracik=0,persenracik=0,kapasitasracik=0;
    private int i=0,j=0,z=0,row2=0,r=0;
    private boolean ubah=false,copy=false,sukses=true;
    private boolean[] pilih; 
    private double[] jumlah,harga,beli,stok,kapasitas,p1,p2;
    private String[] no,kodebarang,namabarang,kodesatuan,kandungan,letakbarang,namajenis,aturan,industri,komposisi;
    public DlgCariAturanPakai aturanpakai=new DlgCariAturanPakai(null,false);
    private WarnaTable2 warna=new WarnaTable2();
    private WarnaTable2 warna2=new WarnaTable2();
    private WarnaTable2 warna3=new WarnaTable2();
    private DlgCariMetodeRacik metoderacik=new DlgCariMetodeRacik(null,false);
    public DlgCariDokter dokter=new DlgCariDokter(null,false);
    private String noracik="",aktifkanbatch="no",STOKKOSONGRESEP="no",resepfarmasi="no",qrystokkosong="",tampilkan_ppnobat_ralan="",status="",bangsal="",kamar="",norawatibu="",kelas,bangsaldefault=Sequel.cariIsi("select kd_bangsal from set_lokasi limit 1"),namaobat="",namaobat1="",namaobatracik="",rincianobat="",latin_racikan="",finger="",tgl_lahir="",norm="",bb="",alergi="";

    /** Creates new form DlgPenyakit
     * @param parent
     * @param modal */
    public DlgPeresepanDokterRSMG(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(10,2);
        setSize(656,250);
        tabModeResep=new DefaultTableModel(null,new Object[]{
                "No","Nama Barang","Jumlah","Aturan Pakai"
            }){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if ((colIndex==1)||(colIndex==2)||(colIndex==3)) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             /*Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };*/
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbResep.setModel(tabModeResep);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbResep.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbResep.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 4; i++) {
            TableColumn column = tbResep.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(30);
            }else if(i==1){
                column.setPreferredWidth(300);
            }else if(i==2){
                column.setPreferredWidth(60);
            }else if(i==3){
                column.setPreferredWidth(100);
            }
        }
        warna.kolom=2;
        tbResep.setDefaultRenderer(Object.class,warna);
        
        tabModeResepRacikan=new DefaultTableModel(null,new Object[]{
                "No","Nama Obat","Kd Racik","Metode Racik","Jml.Racik",
                "Aturan Pakai","Keterangan"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = true;
                if ((colIndex==0)||(colIndex==2)||(colIndex==3)) {
                    a=false;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };

        tbObatResepRacikan.setModel(tabModeResepRacikan);
        tbObatResepRacikan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObatResepRacikan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);        
        tbObatResepRacikan.setRowHeight(80);
        
        for (i = 0; i < 7; i++) {
            TableColumn column = tbObatResepRacikan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(25);
            }else if(i==1){
                column.setPreferredWidth(250);
                column.setCellRenderer(new TextAreaRenderer());
                column.setCellEditor(new TextAreaEditor());
            }else if(i==2){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==3){
                column.setPreferredWidth(100);
            }else if(i==4){
                column.setPreferredWidth(60);
            }else if(i==5){
                column.setPreferredWidth(200);
            }else if(i==6){
                column.setPreferredWidth(250);
            }
        }

        warna2.kolom=4;
        tbObatResepRacikan.setDefaultRenderer(Object.class,warna2);
                
        aturanpakai.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(aturanpakai.getTable().getSelectedRow()!= -1){  
                    if(TabRawat.getSelectedIndex()==0){
                        tbResep.setValueAt(aturanpakai.getTable().getValueAt(aturanpakai.getTable().getSelectedRow(),0).toString(),tbResep.getSelectedRow(),8);
                    }else if(TabRawat.getSelectedIndex()==1){
                        tbObatResepRacikan.setValueAt(aturanpakai.getTable().getValueAt(aturanpakai.getTable().getSelectedRow(),0).toString(),tbObatResepRacikan.getSelectedRow(),5);
                        tbObatResepRacikan.requestFocus();
                    }   
                }   
                tbResep.requestFocus();
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
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){        
                     KdDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                     NmDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                }  
                KdDokter.requestFocus();
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
        
        metoderacik.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(metoderacik.getTable().getSelectedRow()!= -1){  
                    tbObatResepRacikan.setValueAt(metoderacik.getTable().getValueAt(metoderacik.getTable().getSelectedRow(),1).toString(),tbObatResepRacikan.getSelectedRow(),2);
                    tbObatResepRacikan.setValueAt(metoderacik.getTable().getValueAt(metoderacik.getTable().getSelectedRow(),2).toString(),tbObatResepRacikan.getSelectedRow(),3);
                    tbObatResepRacikan.requestFocus();
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
        
        metoderacik.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    metoderacik.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        }); 
        jam();
        
        tampilkan_ppnobat_ralan=Sequel.cariIsi("select tampilkan_ppnobat_ralan from set_nota"); 
        
        try {
            aktifkanbatch = koneksiDB.AKTIFKANBATCHOBAT();
            STOKKOSONGRESEP = koneksiDB.STOKKOSONGRESEP();
            resepfarmasi = koneksiDB.RESEPFARMASI();
        } catch (Exception e) {
            System.out.println("E : "+e);
            aktifkanbatch = "no";
            STOKKOSONGRESEP="no";
            resepfarmasi="no";
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

        Popup = new javax.swing.JPopupMenu();
        ppBersihkan = new javax.swing.JMenuItem();
        ppStok1 = new javax.swing.JMenuItem();
        KdPj = new widget.TextBox();
        LPpn = new widget.Label();
        jLabel6 = new widget.Label();
        internalFrame1 = new widget.InternalFrame();
        panelisi3 = new widget.panelisi();
        BtnTambah2 = new widget.Button();
        BtnTambah1 = new widget.Button();
        BtnHapus = new widget.Button();
        label12 = new widget.Label();
        Jeniskelas = new widget.ComboBox();
        BtnSimpan = new widget.Button();
        BtnKeluar = new widget.Button();
        FormInput = new widget.PanelBiasa();
        TPenjab = new widget.TextBox();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        KdDokter = new widget.TextBox();
        NmDokter = new widget.TextBox();
        jLabel3 = new widget.Label();
        jLabel13 = new widget.Label();
        btnDokter = new widget.Button();
        jLabel11 = new widget.Label();
        NoResep = new widget.TextBox();
        jLabel8 = new widget.Label();
        DTPBeri = new widget.Tanggal();
        cmbJam = new widget.ComboBox();
        cmbMnt = new widget.ComboBox();
        cmbDtk = new widget.ComboBox();
        ChkRM = new widget.CekBox();
        ChkJln = new widget.CekBox();
        label13 = new widget.Label();
        TabRawat = new javax.swing.JTabbedPane();
        Scroll = new widget.ScrollPane();
        tbResep = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        Scroll1 = new widget.ScrollPane();
        tbObatResepRacikan = new widget.Table();

        Popup.setName("Popup"); // NOI18N

        ppBersihkan.setBackground(new java.awt.Color(255, 255, 254));
        ppBersihkan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkan.setForeground(new java.awt.Color(50, 50, 50));
        ppBersihkan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBersihkan.setText("Bersihkan Jumlah");
        ppBersihkan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkan.setName("ppBersihkan"); // NOI18N
        ppBersihkan.setPreferredSize(new java.awt.Dimension(180, 25));
        ppBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBersihkanActionPerformed(evt);
            }
        });
        Popup.add(ppBersihkan);

        ppStok1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppStok1.setForeground(new java.awt.Color(50, 50, 50));
        ppStok1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppStok1.setText("Cek Stok Lokasi");
        ppStok1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppStok1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppStok1.setName("ppStok1"); // NOI18N
        ppStok1.setPreferredSize(new java.awt.Dimension(180, 25));
        ppStok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppStok1ActionPerformed(evt);
            }
        });
        Popup.add(ppStok1);

        KdPj.setHighlighter(null);
        KdPj.setName("KdPj"); // NOI18N

        LPpn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LPpn.setText("0");
        LPpn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        LPpn.setName("LPpn"); // NOI18N
        LPpn.setPreferredSize(new java.awt.Dimension(65, 23));

        jLabel6.setText("PPN :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(35, 23));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Peresepan Obat Oleh Dokter ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelisi3.setName("panelisi3"); // NOI18N
        panelisi3.setPreferredSize(new java.awt.Dimension(100, 43));
        panelisi3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        BtnTambah2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambah2.setMnemonic('3');
        BtnTambah2.setText("Tambah Obat");
        BtnTambah2.setToolTipText("Alt+3");
        BtnTambah2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        BtnTambah2.setName("BtnTambah2"); // NOI18N
        BtnTambah2.setPreferredSize(new java.awt.Dimension(150, 23));
        BtnTambah2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambah2ActionPerformed(evt);
            }
        });
        panelisi3.add(BtnTambah2);

        BtnTambah1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        BtnTambah1.setMnemonic('3');
        BtnTambah1.setText("Tambah Racikan");
        BtnTambah1.setToolTipText("Alt+3");
        BtnTambah1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        BtnTambah1.setName("BtnTambah1"); // NOI18N
        BtnTambah1.setPreferredSize(new java.awt.Dimension(150, 23));
        BtnTambah1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambah1ActionPerformed(evt);
            }
        });
        panelisi3.add(BtnTambah1);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/delete-16x16.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(85, 23));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        panelisi3.add(BtnHapus);

        label12.setText("Tarif :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(50, 23));
        panelisi3.add(label12);

        Jeniskelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rawat Jalan", "Beli Luar", "Karyawan", "Utama/BPJS", "Kelas 1", "Kelas 2", "Kelas 3", "VIP", "VVIP" }));
        Jeniskelas.setName("Jeniskelas"); // NOI18N
        Jeniskelas.setPreferredSize(new java.awt.Dimension(120, 23));
        Jeniskelas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JeniskelasItemStateChanged(evt);
            }
        });
        Jeniskelas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JeniskelasKeyPressed(evt);
            }
        });
        panelisi3.add(Jeniskelas);

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(85, 23));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        panelisi3.add(BtnSimpan);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('5');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+5");
        BtnKeluar.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(80, 23));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        panelisi3.add(BtnKeluar);

        internalFrame1.add(panelisi3, java.awt.BorderLayout.PAGE_END);

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(440, 107));
        FormInput.setLayout(null);

        TPenjab.setHighlighter(null);
        TPenjab.setName("TPenjab"); // NOI18N
        TPenjab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPenjabKeyPressed(evt);
            }
        });
        FormInput.add(TPenjab);
        TPenjab.setBounds(520, 40, 130, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(75, 12, 120, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        FormInput.add(TPasien);
        TPasien.setBounds(196, 12, 487, 23);

        KdDokter.setHighlighter(null);
        KdDokter.setName("KdDokter"); // NOI18N
        KdDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdDokterKeyPressed(evt);
            }
        });
        FormInput.add(KdDokter);
        KdDokter.setBounds(75, 72, 120, 23);

        NmDokter.setEditable(false);
        NmDokter.setHighlighter(null);
        NmDokter.setName("NmDokter"); // NOI18N
        FormInput.add(NmDokter);
        NmDokter.setBounds(196, 72, 230, 23);

        jLabel3.setText("No.Rawat :");
        jLabel3.setName("jLabel3"); // NOI18N
        FormInput.add(jLabel3);
        jLabel3.setBounds(0, 12, 72, 23);

        jLabel13.setText("Peresep :");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(0, 72, 72, 23);

        btnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokter.setMnemonic('3');
        btnDokter.setToolTipText("Alt+3");
        btnDokter.setName("btnDokter"); // NOI18N
        btnDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokterActionPerformed(evt);
            }
        });
        btnDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDokterKeyPressed(evt);
            }
        });
        FormInput.add(btnDokter);
        btnDokter.setBounds(428, 72, 28, 23);

        jLabel11.setText("No.Resep :");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(455, 72, 70, 23);

        NoResep.setHighlighter(null);
        NoResep.setName("NoResep"); // NOI18N
        NoResep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoResepKeyPressed(evt);
            }
        });
        FormInput.add(NoResep);
        NoResep.setBounds(528, 72, 130, 23);

        jLabel8.setText("Tgl.Resep :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(0, 42, 72, 23);

        DTPBeri.setForeground(new java.awt.Color(50, 70, 50));
        DTPBeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "27-06-2022" }));
        DTPBeri.setDisplayFormat("dd-MM-yyyy");
        DTPBeri.setName("DTPBeri"); // NOI18N
        DTPBeri.setOpaque(false);
        DTPBeri.setPreferredSize(new java.awt.Dimension(100, 23));
        DTPBeri.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPBeriItemStateChanged(evt);
            }
        });
        DTPBeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPBeriKeyPressed(evt);
            }
        });
        FormInput.add(DTPBeri);
        DTPBeri.setBounds(75, 42, 90, 23);

        cmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbJam.setName("cmbJam"); // NOI18N
        cmbJam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJamKeyPressed(evt);
            }
        });
        FormInput.add(cmbJam);
        cmbJam.setBounds(168, 42, 62, 23);

        cmbMnt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMnt.setName("cmbMnt"); // NOI18N
        cmbMnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMntKeyPressed(evt);
            }
        });
        FormInput.add(cmbMnt);
        cmbMnt.setBounds(233, 42, 62, 23);

        cmbDtk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbDtk.setName("cmbDtk"); // NOI18N
        cmbDtk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbDtkKeyPressed(evt);
            }
        });
        FormInput.add(cmbDtk);
        cmbDtk.setBounds(298, 42, 62, 23);

        ChkRM.setBorder(null);
        ChkRM.setSelected(true);
        ChkRM.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkRM.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkRM.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkRM.setName("ChkRM"); // NOI18N
        ChkRM.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChkRMItemStateChanged(evt);
            }
        });
        FormInput.add(ChkRM);
        ChkRM.setBounds(660, 72, 23, 23);

        ChkJln.setBorder(null);
        ChkJln.setSelected(true);
        ChkJln.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkJln.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkJln.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkJln.setName("ChkJln"); // NOI18N
        ChkJln.setPreferredSize(new java.awt.Dimension(22, 23));
        ChkJln.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkJlnActionPerformed(evt);
            }
        });
        FormInput.add(ChkJln);
        ChkJln.setBounds(363, 42, 23, 23);

        label13.setText("Jenis Bayar :");
        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput.add(label13);
        label13.setBounds(410, 40, 100, 23);

        internalFrame1.add(FormInput, java.awt.BorderLayout.PAGE_START);

        TabRawat.setBackground(new java.awt.Color(255, 255, 253));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        Scroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll.setComponentPopupMenu(Popup);
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbResep.setComponentPopupMenu(Popup);
        tbResep.setName("tbResep"); // NOI18N
        tbResep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbResepMouseClicked(evt);
            }
        });
        tbResep.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbResepPropertyChange(evt);
            }
        });
        tbResep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbResepKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbResep);

        TabRawat.addTab("Umum", Scroll);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 102));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);
        Scroll1.setPreferredSize(new java.awt.Dimension(454, 402));

        tbObatResepRacikan.setName("tbObatResepRacikan"); // NOI18N
        tbObatResepRacikan.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbObatResepRacikan.setShowGrid(false);
        tbObatResepRacikan.setShowHorizontalLines(true);
        tbObatResepRacikan.setShowVerticalLines(true);
        tbObatResepRacikan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatResepRacikanMouseClicked(evt);
            }
        });
        tbObatResepRacikan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatResepRacikanKeyPressed(evt);
            }
        });
        Scroll1.setViewportView(tbObatResepRacikan);

        jPanel3.add(Scroll1, java.awt.BorderLayout.CENTER);
        Scroll1.getAccessibleContext().setAccessibleName("");

        TabRawat.addTab("Racikan", jPanel3);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void tbResepMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbResepMouseClicked

}//GEN-LAST:event_tbResepMouseClicked

    private void tbResepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbResepKeyPressed
        
}//GEN-LAST:event_tbResepKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(KdDokter.getText().trim().equals("")||NmDokter.getText().trim().equals("")){
            Valid.textKosong(KdDokter,"Dokter");
        }else if(NoResep.getText().trim().equals("")){
            Valid.textKosong(NoResep,"No.Resep");
        }else if(tbResep.getRowCount() == 0 && tbObatResepRacikan.getRowCount() == 0){
            JOptionPane.showMessageDialog(null,"Maaf, silahkan masukkan terlebih dahulu obat yang mau diberikan...!!!");
        }else{
            int reply = JOptionPane.showConfirmDialog(rootPane,"Eeiiiiiits, udah bener belum data yang mau disimpan..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {   
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                ChkJln.setSelected(false);    
                Sequel.AutoComitFalse();
                sukses=true;
                if(ubah==false){
                    if(Sequel.menyimpantf2("resep_obat","?,?,?,?,?,?,?,?","Nomer Resep",8,new String[]{
                        NoResep.getText(),Valid.SetTgl(DTPBeri.getSelectedItem()+""),
                        cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                        TNoRw.getText(),KdDokter.getText(),Valid.SetTgl(DTPBeri.getSelectedItem()+""),
                        cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),status
                        })==true){
                            simpandata();
                    }else{
                        emptTeksobat();
                        if(Sequel.menyimpantf2("resep_obat","?,?,?,?,?,?,?,?","Nomer Resep",8,new String[]{
                            NoResep.getText(),Valid.SetTgl(DTPBeri.getSelectedItem()+""),
                            cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                            TNoRw.getText(),KdDokter.getText(),Valid.SetTgl(DTPBeri.getSelectedItem()+""),
                            cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),status
                            })==true){
                                simpandata();
                        }else{
                            emptTeksobat();
                            if(Sequel.menyimpantf2("resep_obat","?,?,?,?,?,?,?,?","Nomer Resep",8,new String[]{
                                NoResep.getText(),Valid.SetTgl(DTPBeri.getSelectedItem()+""),
                                cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),
                                TNoRw.getText(),KdDokter.getText(),Valid.SetTgl(DTPBeri.getSelectedItem()+""),
                                cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem(),status
                                })==true){
                                    simpandata();
                            }else{
                                emptTeksobat();
                                sukses=false;
                            }
                        }
                    }
                }else if(ubah==true){
                    Sequel.meghapus("resep_dokter","no_resep",NoResep.getText());
                    Sequel.meghapus("resep_dokter_racikan","no_resep",NoResep.getText());
                    Sequel.meghapus("resep_dokter_racikan_detail","no_resep",NoResep.getText());
                    Sequel.meghapus("resep_template","no_resep",NoResep.getText());
                    ubah=false;
                    simpandata();
                }                                                      
                
                if(sukses==true){
                    Sequel.Commit();
                    for(i=0;i<tbResep.getRowCount();i++){
                        tbResep.setValueAt("",i,1);
                    }
                    Valid.tabelKosong(tabModeResepRacikan);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(null,"Terjadi kesalahan saat pemrosesan data, transaksi dibatalkan.\nPeriksa kembali data sebelum melanjutkan menyimpan..!!");
                    Sequel.RollBack();
                }
                
                if(sukses == true){
                    
                    if(resepfarmasi.equals("yes")){

                    DlgDaftarPermintaanResep daftar=new DlgDaftarPermintaanResep(null,false);
                    daftar.emptTeks();
                    daftar.isCek();
                    daftar.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                    daftar.setLocationRelativeTo(internalFrame1);
                    daftar.setNoRm(TNoRw.getText());
                    daftar.setVisible(true);
                    
                    }else{

                    try{
                        //Umum
                        Sequel.queryu("truncate table temporary_resep");
                        ps=koneksi.prepareStatement(
                            "SELECT " +
                            "resep_dokter_rsmg.nm_obat,resep_dokter_rsmg.jml,resep_dokter_rsmg.aturan_pakai " +
                            "FROM resep_dokter_rsmg " +
                            "WHERE resep_dokter_rsmg.no_resep=?" +
                            "ORDER BY resep_dokter_rsmg.nm_obat ASC");
                        try{
                            ps.setString(1, NoResep.getText());
                            rs=ps.executeQuery();
                            while(rs.next()){
                                namaobat = rs.getString("nm_obat").split("/")[0];
                                Sequel.menyimpan("temporary_resep","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?",38,new String[]{
                                    "0",namaobat,rs.getString("aturan_pakai"),"No. "+intToRoman(rs.getInt("jml")),"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
                                });
                            }
                        } catch (Exception e) {
                            System.out.println("Notif Resep Umum = "+e);
                        } finally{
                            if(rs!=null){
                                rs.close();
                            }
                            if(ps!=null){
                                ps.close();
                            }
                        }
                        
                        //Racikan
                        psracikan=koneksi.prepareStatement(
                            "select resep_dokter_racikan_rsmg.no_racikan,resep_dokter_racikan_rsmg.nama_racik,resep_dokter_racikan_rsmg.kd_racik, " +
                            "resep_dokter_racikan_rsmg.jml,resep_dokter_racikan_rsmg.aturan_pakai,resep_dokter_racikan_rsmg.keterangan " +
                            "from resep_dokter_racikan_rsmg " +
                            "where resep_dokter_racikan_rsmg.no_resep=? order by resep_dokter_racikan_rsmg.no_racikan ASC");
                        try{
                            psracikan.setString(1, NoResep.getText());
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
                                    psracikan2.setString(2, NoResep.getText());
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
                    param.put("penanggung",Sequel.cariIsi("select png_jawab from penjab where kd_pj=?",Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",TNoRw.getText())));               
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("tanggal",Valid.SetTgl(DTPBeri.getSelectedItem()+""));
                    param.put("norawat",TNoRw.getText());
                    param.put("pasien",TPasien.getText());
                    bb=Sequel.cariIsi("select berat from pemeriksaan_ralan where no_rawat =?",TNoRw.getText());
                    if(bb.equalsIgnoreCase("")){
                        param.put("bb","-");
                    }else{
                        param.put("bb",bb);
                    }
                    alergi=Sequel.cariIsi("select alergi from pemeriksaan_ralan where no_rawat=?",TNoRw.getText());
                    param.put("alergi",alergi);
                    norm=Sequel.cariIsi("select no_rkm_medis from reg_periksa where no_rawat =?", TNoRw.getText());
                    param.put("norm",norm);
                    param.put("peresep",NmDokter.getText());
                    param.put("noresep",NoResep.getText());
                    tgl_lahir=Sequel.cariIsi("select tgl_lahir from pasien where no_rkm_medis =?",norm);
                    param.put("tgl_lahir",tgl_lahir);
                    finger=Sequel.cariIsi("select sha1(sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",KdDokter.getText());
                    param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+NmDokter.getText()+"\nID "+(finger.equals("")?KdDokter.getText():finger)+"\n"+DTPBeri.getSelectedItem());  
                    param.put("jam",cmbJam.getSelectedItem()+":"+cmbMnt.getSelectedItem()+":"+cmbDtk.getSelectedItem());
                    param.put("logo",Sequel.cariGambar("select logo from setting")); 

                    Valid.MyReport("rptEResep.jasper",param,"::[ Lembar Pemberian Obat Resep Dokter]::");

                }                
                Sequel.AutoComitTrue();
                ChkJln.setSelected(true);
                }  
            
            }              
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

private void ppBersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkanActionPerformed
    if(TabRawat.getSelectedIndex()==0){
        for(i=0;i<tbResep.getRowCount();i++){ 
            tbResep.setValueAt("",i,1);
            tbResep.setValueAt(0,i,10);
            tbResep.setValueAt(0,i,9);
            tbResep.setValueAt(0,i,8);
        }
    } 
}//GEN-LAST:event_ppBersihkanActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        TabRawatMouseClicked(null);
        if(ubah==false){
            emptTeksobat();
        }
            
    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
    }//GEN-LAST:event_formWindowOpened

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select concat(pasien.no_rkm_medis,' ',pasien.nm_pasien) from reg_periksa inner join pasien "+
                " on reg_periksa.no_rkm_medis=pasien.no_rkm_medis where no_rawat=? ",TPasien,TNoRw.getText());
        }else{
            Valid.pindah(evt,KdDokter,DTPBeri);
        }
    }//GEN-LAST:event_TNoRwKeyPressed

    private void KdDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDokterKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",NmDokter,KdDokter.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            btnDokterActionPerformed(null);
        }else{
            Valid.pindah(evt,NoResep,BtnSimpan);
        }
    }//GEN-LAST:event_KdDokterKeyPressed

    private void btnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokterActionPerformed
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.isCek();
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_btnDokterActionPerformed

    private void btnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDokterKeyPressed
        Valid.pindah(evt,KdDokter,BtnSimpan);
    }//GEN-LAST:event_btnDokterKeyPressed

    private void NoResepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoResepKeyPressed
        Valid.pindah(evt,cmbDtk,KdDokter);
    }//GEN-LAST:event_NoResepKeyPressed

    private void DTPBeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPBeriKeyPressed
        Valid.pindah(evt,TNoRw,cmbJam);
    }//GEN-LAST:event_DTPBeriKeyPressed

    private void cmbJamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJamKeyPressed
        Valid.pindah(evt,DTPBeri,cmbMnt);
    }//GEN-LAST:event_cmbJamKeyPressed

    private void cmbMntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMntKeyPressed
        Valid.pindah(evt,cmbJam,cmbDtk);
    }//GEN-LAST:event_cmbMntKeyPressed

    private void cmbDtkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDtkKeyPressed
        Valid.pindah(evt,cmbMnt,NoResep);
    }//GEN-LAST:event_cmbDtkKeyPressed

    private void ChkRMItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChkRMItemStateChanged
        if(ChkRM.isSelected()==true){
            NoResep.setEditable(false);
            NoResep.setBackground(new Color(245,250,240));
            emptTeksobat();
        }else if(ChkRM.isSelected()==false){
            NoResep.setEditable(true);
            NoResep.setBackground(new Color(250,255,245));
            NoResep.setText("");
        }
    }//GEN-LAST:event_ChkRMItemStateChanged

    private void ChkJlnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkJlnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkJlnActionPerformed

    private void JeniskelasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JeniskelasItemStateChanged

    }//GEN-LAST:event_JeniskelasItemStateChanged

    private void JeniskelasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JeniskelasKeyPressed

    }//GEN-LAST:event_JeniskelasKeyPressed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==0){
            BtnTambah2.setVisible(true);
            BtnTambah1.setVisible(false);
            BtnHapus.setVisible(true);
        }else if(TabRawat.getSelectedIndex()==1){
            BtnTambah2.setVisible(false);
            BtnTambah1.setVisible(true);
            BtnHapus.setVisible(true);
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void tbObatResepRacikanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatResepRacikanKeyPressed
        if(tbObatResepRacikan.getRowCount()!=0){
            i=tbObatResepRacikan.getSelectedColumn();
            if(evt.getKeyCode()==KeyEvent.VK_RIGHT){
                if(i==5){
                    akses.setform("DlgCariObat");
                    aturanpakai.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                    aturanpakai.setLocationRelativeTo(internalFrame1);
                    aturanpakai.setVisible(true);
                }else if(i==3){
                    if(tbObatResepRacikan.getValueAt(tbObatResepRacikan.getSelectedRow(),1).equals("")){
                        JOptionPane.showMessageDialog(null,"Silahkan masukkan nama racikan..!!");
                        tbObatResepRacikan.requestFocus();
                    }else{
                        metoderacik.isCek();
                        metoderacik.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                        metoderacik.setLocationRelativeTo(internalFrame1);
                        metoderacik.setVisible(true);
                    }
                }
            }
        }
    }//GEN-LAST:event_tbObatResepRacikanKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(TabRawat.getSelectedIndex()==0){
            if(tbResep.getValueAt(tbResep.getSelectedRow(),1).equals("")&&tbResep.getValueAt(tbResep.getSelectedRow(),2).equals("")&&tbResep.getValueAt(tbResep.getSelectedRow(),3).equals("")){
                tabModeResep.removeRow(tbResep.getSelectedRow());
            }else{
                JOptionPane.showMessageDialog(null,"Maaf sudah terisi, gak boleh dihapus..!!");
            }
        }else if(TabRawat.getSelectedIndex()==1){
            if(tbObatResepRacikan.getValueAt(tbObatResepRacikan.getSelectedRow(),1).equals("")&&tbObatResepRacikan.getValueAt(tbObatResepRacikan.getSelectedRow(),4).equals("")&&tbObatResepRacikan.getValueAt(tbObatResepRacikan.getSelectedRow(),5).equals("")&&tbObatResepRacikan.getValueAt(tbObatResepRacikan.getSelectedRow(),6).equals("")){
                tabModeResepRacikan.removeRow(tbObatResepRacikan.getSelectedRow());
            }else{
                JOptionPane.showMessageDialog(null,"Maaf sudah terisi, gak boleh dihapus..!!");
            }
        }

    }//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnTambah1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambah1ActionPerformed
        i=tabModeResepRacikan.getRowCount()+1;

        if(i==99){
            JOptionPane.showMessageDialog(null,"Maksimal 98 Racikan..!!");
        }else{
            tabModeResepRacikan.addRow(new Object[]{""+i,"","","","","",""});
        }
    }//GEN-LAST:event_BtnTambah1ActionPerformed

    private void tbResepPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbResepPropertyChange

    }//GEN-LAST:event_tbResepPropertyChange

    private void ppStok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppStok1ActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DlgCekStok ceksetok=new DlgCekStok(null,false);
        ceksetok.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        ceksetok.setLocationRelativeTo(internalFrame1);
        ceksetok.setAlwaysOnTop(false);
        ceksetok.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_ppStok1ActionPerformed

    private void DTPBeriItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPBeriItemStateChanged
        try {
            emptTeksobat();
        } catch (Exception e) {
        }
            
    }//GEN-LAST:event_DTPBeriItemStateChanged

    private void tbObatResepRacikanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatResepRacikanMouseClicked
            if(tabModeResepRacikan.getRowCount()!=0){
                if(evt.getClickCount()==2){
                    i=tbObatResepRacikan.getSelectedColumn();
                    if(i==3){
                        if(tbObatResepRacikan.getValueAt(tbObatResepRacikan.getSelectedRow(),1).equals("")){
                            JOptionPane.showMessageDialog(null,"Silahkan masukkan nama racikan..!!");
                            tbObatResepRacikan.requestFocus();
                        }else{
                            metoderacik.isCek();
                            metoderacik.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                            metoderacik.setLocationRelativeTo(internalFrame1);
                            metoderacik.setVisible(true);
                        }
                    }
                }
            }
    }//GEN-LAST:event_tbObatResepRacikanMouseClicked

    private void BtnTambah2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambah2ActionPerformed
        j=tabModeResep.getRowCount()+1;
        
        if(j==99){
                JOptionPane.showMessageDialog(null,"Maksimal 98 Racikan..!!");
        }else{
                tabModeResep.addRow(new Object[]{""+j,"","",""});
        }
    }//GEN-LAST:event_BtnTambah2ActionPerformed

    private void TPenjabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPenjabKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TPenjabKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgPeresepanDokterRSMG dialog = new DlgPeresepanDokterRSMG(new javax.swing.JFrame(), true);
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
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnSimpan;
    private widget.Button BtnTambah1;
    private widget.Button BtnTambah2;
    private widget.CekBox ChkJln;
    private widget.CekBox ChkRM;
    private widget.Tanggal DTPBeri;
    private widget.PanelBiasa FormInput;
    private widget.ComboBox Jeniskelas;
    private widget.TextBox KdDokter;
    private widget.TextBox KdPj;
    private widget.Label LPpn;
    private widget.TextBox NmDokter;
    private widget.TextBox NoResep;
    private javax.swing.JPopupMenu Popup;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPenjab;
    private javax.swing.JTabbedPane TabRawat;
    private widget.Button btnDokter;
    private widget.ComboBox cmbDtk;
    private widget.ComboBox cmbJam;
    private widget.ComboBox cmbMnt;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel11;
    private widget.Label jLabel13;
    private widget.Label jLabel3;
    private widget.Label jLabel6;
    private widget.Label jLabel8;
    private javax.swing.JPanel jPanel3;
    private widget.Label label12;
    private widget.Label label13;
    private widget.panelisi panelisi3;
    private javax.swing.JMenuItem ppBersihkan;
    private javax.swing.JMenuItem ppStok1;
    private widget.Table tbObatResepRacikan;
    private widget.Table tbResep;
    // End of variables declaration//GEN-END:variables


    public void emptTeksobat() {
        if(ChkRM.isSelected()==true){
            Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(no_resep,4),signed)),0) from resep_obat where tgl_perawatan='"+Valid.SetTgl(DTPBeri.getSelectedItem()+"")+"' ",
                DTPBeri.getSelectedItem().toString().substring(6,10)+DTPBeri.getSelectedItem().toString().substring(3,5)+DTPBeri.getSelectedItem().toString().substring(0,2),4,NoResep);        
        } 
    }

    public JTable getTable(){
        return tbResep;
    }
    
    public Button getButton(){
        return BtnSimpan;
    }
    
    public void isCek(){   
        if(status.equals("ralan")){
            bangsal=Sequel.cariIsi("select kd_bangsal from set_depo_ralan where kd_poli=?",Sequel.cariIsi("select kd_poli from reg_periksa where no_rawat=?",TNoRw.getText()));
            if(bangsal.equals("")){
                bangsal=bangsaldefault;
            }
        }else if(status.equals("ranap")){
            bangsal=akses.getkdbangsal();
        }  
    }
    
    public void setNoRm(String norwt,Date tanggal, String jam,String menit,String detik,String KodeDokter,String NamaDokter,String status) {        
        TNoRw.setText(norwt);
        Sequel.cariIsi("select concat(pasien.no_rkm_medis,' ',pasien.nm_pasien) from reg_periksa inner join pasien "+
                    " on reg_periksa.no_rkm_medis=pasien.no_rkm_medis where no_rawat=? ",TPasien,TNoRw.getText());
        Sequel.cariIsi("select penjab.png_jawab from reg_periksa inner join penjab "+
                    " on reg_periksa.kd_pj = penjab.kd_pj where reg_periksa.no_rawat=? ",TPenjab,TNoRw.getText());
        DTPBeri.setDate(tanggal);
        cmbJam.setSelectedItem(jam);
        cmbMnt.setSelectedItem(menit);
        cmbDtk.setSelectedItem(detik); 
        KdDokter.setText(KodeDokter);
        NmDokter.setText(NamaDokter);
        KdPj.setText(Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",norwt));
        this.status=status;
        SetHarga();
        ubah=false;
        copy=false;
    }
    
    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            @Override
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                // Membuat Date
                //Date dt = new Date();
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if(ChkJln.isSelected()==true){
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                }else if(ChkJln.isSelected()==false){
                    nilai_jam =cmbJam.getSelectedIndex();
                    nilai_menit =cmbMnt.getSelectedIndex();
                    nilai_detik =cmbDtk.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                cmbJam.setSelectedItem(jam);
                cmbMnt.setSelectedItem(menit);
                cmbDtk.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }

    private void simpandata() {
        try {
            for(i=0;i<tbResep.getRowCount();i++){ 
                if(Valid.SetAngka(tbResep.getValueAt(i,2).toString())>0){                        
                    if(Sequel.menyimpantf2("resep_dokter_rsmg","?,?,?,?","data",4,new String[]{
                        NoResep.getText(),tbResep.getValueAt(i,1).toString(),
                        ""+(Double.parseDouble(tbResep.getValueAt(i,2).toString())),
                        tbResep.getValueAt(i,3).toString()
                    })==false){
                        sukses=false;
                    }                                                         
                }
            } 

            for(i=0;i<tbObatResepRacikan.getRowCount();i++){ 
                if(Valid.SetAngka(tbObatResepRacikan.getValueAt(i,4).toString())>0){ 
                    if(Sequel.menyimpantf2("resep_dokter_racikan_rsmg","?,?,?,?,?,?,?","resep obat racikan",7,new String[]{
                       NoResep.getText(),tbObatResepRacikan.getValueAt(i,0).toString(),tbObatResepRacikan.getValueAt(i,1).toString(),
                       tbObatResepRacikan.getValueAt(i,3).toString(),tbObatResepRacikan.getValueAt(i,4).toString(),
                       tbObatResepRacikan.getValueAt(i,5).toString(),tbObatResepRacikan.getValueAt(i,6).toString()
                    })==false){
                        sukses=false;
                    } 
                }
            }

        } catch (Exception e) {
            System.out.println("Notif : "+e);
        } 
    }
    
    public void MatikanJam(){
        ChkJln.setSelected(false);
    }

    private void SetHarga() {
        if(status.equals("ranap")){
            norawatibu=Sequel.cariIsi("select no_rawat from ranap_gabung where no_rawat2=?",TNoRw.getText());
            if(!norawatibu.equals("")){
                kamar=Sequel.cariIsi("select ifnull(kd_kamar,'') from kamar_inap where no_rawat=? order by tgl_masuk desc limit 1",norawatibu);
            }else{
                kamar=Sequel.cariIsi("select ifnull(kd_kamar,'') from kamar_inap where no_rawat=? order by tgl_masuk desc limit 1",TNoRw.getText());
            }
            if(!norawatibu.equals("")){
                kelas=Sequel.cariIsi(
                    "select kamar.kelas from kamar inner join kamar_inap "+
                    "on kamar.kd_kamar=kamar_inap.kd_kamar where no_rawat=? "+
                    "and stts_pulang='-' order by STR_TO_DATE(concat(kamar_inap.tgl_masuk,' ',jam_masuk),'%Y-%m-%d %H:%i:%s') desc limit 1",norawatibu);
            }else{
                kelas=Sequel.cariIsi(
                    "select kamar.kelas from kamar inner join kamar_inap "+
                    "on kamar.kd_kamar=kamar_inap.kd_kamar where no_rawat=? "+
                    "and stts_pulang='-' order by STR_TO_DATE(concat(kamar_inap.tgl_masuk,' ',jam_masuk),'%Y-%m-%d %H:%i:%s') desc limit 1",TNoRw.getText());
            }                
            if(kelas.equals("Kelas 1")){
                Jeniskelas.setSelectedItem("Kelas 1");
            }else if(kelas.equals("Kelas 2")){
                Jeniskelas.setSelectedItem("Kelas 2");
            }else if(kelas.equals("Kelas 3")){
                Jeniskelas.setSelectedItem("Kelas 3");
            }else if(kelas.equals("Kelas Utama")){
                Jeniskelas.setSelectedItem("Utama/BPJS");
            }else if(kelas.equals("Kelas VIP")){
                Jeniskelas.setSelectedItem("VIP");
            }else if(kelas.equals("Kelas VVIP")){
                Jeniskelas.setSelectedItem("VVIP");
            } 
            kenaikan=Sequel.cariIsiAngka2("select (hargajual/100) from  set_harga_obat_ranap where kd_pj=? and kelas=?",KdPj.getText(),kelas);
        }else if(status.equals("ralan")){
            kelas="Rawat Jalan";
            kenaikan=Sequel.cariIsiAngka("select (hargajual/100) from set_harga_obat_ralan where kd_pj=?",KdPj.getText());
        }
    }
    
}

class TextAreaRenderer extends JScrollPane implements TableCellRenderer {
   JTextArea textarea;
   public TextAreaRenderer() {
      textarea = new JTextArea();
      textarea.setLineWrap(true);
      textarea.setWrapStyleWord(true);
      getViewport().add(textarea);
   }
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
      if (isSelected) {
         setForeground(table.getSelectionForeground());
         setBackground(table.getSelectionBackground());
         textarea.setForeground(table.getSelectionForeground());
         textarea.setBackground(table.getSelectionBackground());
      } else {
         setForeground(table.getForeground());
         setBackground(table.getBackground());
         textarea.setForeground(table.getForeground());
         textarea.setBackground(table.getBackground());
      }
      textarea.setText((String) value);
      textarea.setCaretPosition(0);
      return this;
   }
}

class TextAreaEditor extends DefaultCellEditor {
   protected JScrollPane scrollpane;
   protected JTextArea textarea;
   public TextAreaEditor() {
      super(new JCheckBox());
      scrollpane = new JScrollPane();
      textarea = new JTextArea();
      textarea.setLineWrap(true);
      textarea.setWrapStyleWord(true);
      scrollpane.getViewport().add(textarea);
   }
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      textarea.setText((String) value);
      return scrollpane;
   }
   public Object getCellEditorValue() {
      return textarea.getText();
   }
}