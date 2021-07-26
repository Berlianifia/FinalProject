
package bengkel;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class Transaksi_Service extends javax.swing.JInternalFrame {
private Connection conn = new Koneksi().connect();
        private DefaultTableModel tabmode;
        int hrg, sub, jml, ongkos; 
    public Transaksi_Service() {
        initComponents();
        non_aktif();
        tampilComboplg();
        tampilCombomekanik();
        tampilCombosp();
        Date date = new Date();
        jDateChooser1.setDate(date);
        autoNumber();
        dataTabel();
        tabmode =new DefaultTableModel();
        jTable1.setModel(tabmode);
        tabmode.addColumn("Kode");
        tabmode.addColumn("Nama Sparepart");
        tabmode.addColumn("Harga");
        tabmode.addColumn("jumlah");
        tabmode.addColumn("Ongkos");
        tabmode.addColumn("Sub Total");
    }
    
    
    private void tampilCombosp(){
        String sql = "SELECT * FROM sparepart";
        try {
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while(hasil.next()){
                cbkode.addItem(hasil.getString("kd_sparepart"));
            }
            hasil.close();
            stat.close();
            } catch (SQLException ex) {
        }         
    }
      
     private void kosong(){
    cbkode.setSelectedItem("Sparepart");
    txHarga.setText("");
    txjumlah.setText("");
    txStok.setText("0");
    txTotal.setText("");
    txKembalian.setText("");
    txBayar.setText("");
    txongkos.setText("");

    }
    private void non_aktif(){
        txNo.setEnabled(false);
        txHarga.setEnabled(false);
        txStok.setEnabled(false);
        txplg.setEnabled(false);
        txDateTime.setEnabled(false);
    }
    
     private void kodeSparepart(){
        String sql = "SELECT * FROM sparepart WHERE kd_sparepart = '"+cbkode.getSelectedItem()+"'";
        try {
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while(hasil.next()){
                txHarga.setText(hasil.getString("harga"));
                txongkos.setText(hasil.getString("ongkos"));
                txjumlah.requestFocus();
                txStok.setText(hasil.getString("stok"));
            }
            hasil.close();
            stat.close();
            } catch (SQLException ex) {
        }         
    }
     
      private void autoNumber() {
        try {
            java.util.Date tgl = new java.util.Date();  
            java.text.SimpleDateFormat kal = new java.text.SimpleDateFormat("yyMMdd");  
            java.text.SimpleDateFormat tanggal = new java.text.SimpleDateFormat("yyyyMMdd");  
            String sql = "SELECT MAX(no_faktur) FROM service_motor WHERE tanggal ="+tanggal.format(tgl);
            PreparedStatement stat = conn.prepareStatement(sql);
            ResultSet rs = stat.executeQuery(sql);
            while(rs.next()){  
            Long a =rs.getLong(1); //mengambil nilai tertinggi  
                if(a == 0){  
                    this.txNo.setText(kal.format(tgl)+"0000"+(a+1));  
                }else{  
                    this.txNo.setText(""+(a+1));  
                }  
            }  
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();//penanganan masalah
        }
    }
      
      private void tampilComboplg(){
        String sql = "SELECT * FROM pelanggan";
        try {
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while(hasil.next()){
                cbplg.addItem(hasil.getString("nm_pelanggan"));
            }
            hasil.close();
            stat.close();
            } catch (SQLException ex) {
        }         
    }
      
       private void tampilCombomekanik(){
        String sql = "SELECT * FROM mekanik";
        try {
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while(hasil.next()){
                cbmekanik.addItem(hasil.getString("nm_mekanik"));
            }
            hasil.close();
            stat.close();
            } catch (SQLException ex) {
        }         
    }
      
       private void tampilPlg(){ 
        try {
        String sql = "SELECT kd_pelanggan FROM pelanggan WHERE nm_pelanggan='"+cbplg.getSelectedItem()+"'"; 
        Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while(hasil.next()){
            Object[] ob = new Object[1];
            ob[0]=  hasil.getString(1);
            txplg.setText((String) ob[0]);
        }
            hasil.close(); 
            hasil.close(); 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }     
  
    }
       
        private void tampilMekanik(){ 
        try {
        String sql = "SELECT kd_mekanik FROM mekanik WHERE nm_mekanik='"+cbmekanik.getSelectedItem()+"'"; 
        Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while(hasil.next()){
            Object[] ob = new Object[1];
            ob[0]=  hasil.getString(1);
            txmekanik.setText((String) ob[0]);
        }
            hasil.close(); 
            hasil.close(); 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }     
  
    }
       
        private void dataTabel(){ 
        Object[] Baris ={"Kode","Nama Sparepart","Harga","Jumlah", "Ongkos", "Sub Total"};
        tabmode = new DefaultTableModel(null, Baris);
        jTable1.setModel(tabmode);   
        try {
            String sql = "SELECT * FROM sparepart,detail_service WHERE detail_service.kd_sparepart = sparepart.kd_sparepart "
                + "AND detail_service.no_faktur='"+this.txNo.getText()+"'";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while(hasil.next()){
                String kode = hasil.getString("kd_sparepart");
                String nama = hasil.getString("nm_sparepart");
                String harga = hasil.getString("harga");
                String jumlah = hasil.getString("jumlah");
                String ongkos = hasil.getString("ongkos");
                String subtotal = hasil.getString("subtotal");
                String[] data={kode, nama, harga, jumlah,ongkos, subtotal};
                tabmode.addRow(data);
            }
        } catch (Exception e) {
        }
    //menjumlahkan isi colom sub total
    int total = 0;
    for (int i =0; i< jTable1.getRowCount(); i++){
    int amount = Integer.parseInt((String)jTable1.getValueAt(i, 5));
    total += amount;
    }
    txTotal.setText(""+total);
    }
        
    private void simpanNota(){
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    Date tanggal = new Date(); 
    tanggal = jDateChooser1.getDate(); 
    String jual_tgl = dateFormat.format(tanggal);
    
    String sql = "INSERT INTO service_motor VALUES(?,?,?,?)";
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, txNo.getText());
                stat.setString(2, jual_tgl);
                stat.setString(3, txplg.getText());
                stat.setString(4, txmekanik.getText());
                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Gagal Disimpan "+e);
                kosong();
            }
            autoNumber();
            dataTabel();
    }
         
    private void tambah_item(){
    hrg=Integer.parseInt(txHarga.getText());
    jml=Integer.parseInt(txjumlah.getText());
    ongkos= Integer.parseInt(txongkos.getText());
    sub = hrg*jml+ongkos;
    
    String sql = "INSERT INTO detail_service values(?,?,?,?,?,?)";
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setString(1, (String) cbkode.getSelectedItem());
                stat.setString(2, txHarga.getText());
                stat.setString(3, txjumlah.getText());
                stat.setString(4, txNo.getText());
                stat.setString(5, txongkos.getText());
                stat.setString(6, String.valueOf(sub));
                
                stat.executeUpdate();
                kosong();
                cbkode.requestFocus();
                tambahItem.requestFocus();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Gagal Disimpan "+e);
                kosong();
            }
            non_aktif();       
    }
    
     private void update_stok(){
        int x, y, z;
        x = Integer.parseInt(txStok.getText());
        y = Integer.parseInt(txjumlah.getText());
        z = x-y;
        try{
        String sql ="UPDATE sparepart SET stok=? WHERE kd_sparepart=?";  
        PreparedStatement stat = conn.prepareStatement(sql);
           stat.setInt(1,z);
           stat.setString(2, (String) cbkode.getSelectedItem());
           stat.executeUpdate();  
           stat.close();  
        }catch(SQLException e){  
        System.out.println("Terjadi Kesalahan");  
        }finally{
        JOptionPane.showMessageDialog(this,"Stock sparepart telah di update Diubah");  
        }
    }
     
    private void cariKode(){
    int i=jTable1.getSelectedRow();  
    if(i==-1)  
    { return; }  
    String ID=(String)tabmode.getValueAt(i, 0); 
    cbkode.setSelectedItem(ID);
    }
    
     private void tampilKlik(){
        try {
        String sql="SELECT * FROM sparepart, detail_service WHERE "
                + "detail_service.kd_sparepart = '"+this.cbkode.getSelectedItem()+"'";
        PreparedStatement stat = conn.prepareStatement(sql);
        ResultSet rs = stat.executeQuery(sql);
        while(rs.next()){
        this.txHarga.setText(rs.getString("harga"));
        this.txjumlah.setText(rs.getString("jumlah"));
        this.txongkos.setText(rs.getString("ongkos"));
        }
        rs.close(); 
        stat.close();}
        catch (Exception e) {
            System.out.println(e.getMessage());
        }      
    }
     
     private void tampilStok(){
        try {
        String sql="SELECT * FROM sparepart WHERE kd_sparepart = '"+this.cbkode.getSelectedItem()+"'";
        PreparedStatement stat = conn.prepareStatement(sql);
        ResultSet rs = stat.executeQuery(sql);
        while(rs.next()){
        this.txStok.setText(rs.getString("stok"));
        }
        rs.close(); 
        stat.close();}
        catch (Exception e) {
            System.out.println(e.getMessage());
        }      
    }
     
        private void hapusData(){
        try {
        String sql="DELETE From detail_service WHERE no_faktur='"+txNo.getText()+"'";
        PreparedStatement stat =conn.prepareStatement(sql);
        stat.executeUpdate();
        stat.close();
        }catch(SQLException e){
        System.out.println("Terjadi Kesalahan");
        }finally{
        }  
        try {
        String sql="DELETE FROM service_motor WHERE no_faktur='"+txNo.getText()+"'";
        PreparedStatement stat =conn.prepareStatement(sql);
        stat.executeUpdate();
        stat.close();
        }catch(SQLException e){
        System.out.println("Terjadi Kesalahan");
        }finally{
        dataTabel();
        JOptionPane.showMessageDialog(null,"Sukses Hapus Data...");
        }  
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txNo = new javax.swing.JTextField();
        cbplg = new javax.swing.JComboBox<>();
        txplg = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cbmekanik = new javax.swing.JComboBox();
        txmekanik = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txHarga = new javax.swing.JTextField();
        txjumlah = new javax.swing.JTextField();
        tambahItem = new javax.swing.JButton();
        txStok = new javax.swing.JTextField();
        hapusItem = new javax.swing.JButton();
        cbkode = new javax.swing.JComboBox();
        txongkos = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txDateTime = new javax.swing.JTextField();
        batalNota = new javax.swing.JButton();
        txTotal = new javax.swing.JTextField();
        txBayar = new javax.swing.JTextField();
        txKembalian = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setClosable(true);
        setForeground(new java.awt.Color(255, 255, 255));
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel4.setBackground(new java.awt.Color(255, 204, 0));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setText("TRANSAKSI SERVICE");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(279, 279, 279))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));

        jPanel1.setBackground(new java.awt.Color(255, 204, 0));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setToolTipText("");

        jLabel1.setText("No Nota");

        jLabel2.setText("Tanggal");

        jLabel3.setText("Pelanggan");

        cbplg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pelanggan" }));
        cbplg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbplgMouseClicked(evt);
            }
        });
        cbplg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbplgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txNo, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbplg, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txplg, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbplg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txplg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 204, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel11.setText("Mekanik");

        cbmekanik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mekanik" }));
        cbmekanik.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbmekanikMouseClicked(evt);
            }
        });
        cbmekanik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbmekanikActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbmekanik, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txmekanik, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cbmekanik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txmekanik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 204, 51));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Kode Sparepart");

        jLabel5.setText("Harga");

        jLabel6.setText("Jumlah");

        txjumlah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txjumlahMouseClicked(evt);
            }
        });
        txjumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txjumlahActionPerformed(evt);
            }
        });
        txjumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txjumlahKeyPressed(evt);
            }
        });

        tambahItem.setText("+");
        tambahItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahItemActionPerformed(evt);
            }
        });
        tambahItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tambahItemKeyPressed(evt);
            }
        });

        hapusItem.setText("-");
        hapusItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusItemActionPerformed(evt);
            }
        });

        cbkode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sparepart" }));
        cbkode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbkodeActionPerformed(evt);
            }
        });

        jLabel10.setText("Ongkos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbkode, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel10))
                        .addGap(61, 61, 61)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txongkos, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txStok, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tambahItem)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(hapusItem, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txjumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbkode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txongkos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txjumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hapusItem)
                    .addComponent(tambahItem)))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        batalNota.setText("Batal Transaksi");
        batalNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalNotaActionPerformed(evt);
            }
        });

        txTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N

        txBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txBayarKeyPressed(evt);
            }
        });

        jLabel9.setText("Kembalian");

        jLabel8.setText("Bayar");

        jLabel7.setText("Total");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(batalNota))
                                    .addComponent(txDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(80, 80, 80)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel8))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addComponent(jLabel7)
                                        .addGap(18, 18, 18)
                                        .addComponent(txTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(txDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(batalNota))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(txBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel7))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        setBounds(0, 0, 823, 587);
    }// </editor-fold>//GEN-END:initComponents

    private void cbplgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbplgMouseClicked
        // TODO add your handling code here:
        if (cbplg.getSelectedItem() != "Pelanggan"){
            txjumlah.setEnabled(true);
            tambahItem.setEnabled(true);
        }else{

        }
    }//GEN-LAST:event_cbplgMouseClicked

    private void cbplgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbplgActionPerformed
        // TODO add your handling code here
        tampilPlg();
    }//GEN-LAST:event_cbplgActionPerformed

    private void cbmekanikMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbmekanikMouseClicked
        if (cbmekanik.getSelectedItem() != "Mekanik"){
            txjumlah.setEnabled(true);
            tambahItem.setEnabled(true);
        }else{

        }
    }//GEN-LAST:event_cbmekanikMouseClicked

    private void cbmekanikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbmekanikActionPerformed
        tampilMekanik(); // TODO add your handling code here:
    }//GEN-LAST:event_cbmekanikActionPerformed

    private void txjumlahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txjumlahMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txjumlahMouseClicked

    private void txjumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txjumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txjumlahActionPerformed

    private void txjumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txjumlahKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==KeyEvent.VK_ENTER) {
            int jumlah = Integer.parseInt(txjumlah.getText());
            int stok = Integer.parseInt(txStok.getText());
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                if (stok < jumlah ){
                    JOptionPane.showMessageDialog(null,"Stok Kurang");
                    txjumlah.requestFocus();
                    txjumlah.setText("");
                }else {
                    txongkos.requestFocus();
                    tambahItem.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txjumlahKeyPressed

    private void tambahItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahItemActionPerformed
        tambah_item();
        update_stok();
        dataTabel();
    }//GEN-LAST:event_tambahItemActionPerformed

    private void tambahItemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tambahItemKeyPressed
        update_stok();
        tambah_item();
        dataTabel();
    }//GEN-LAST:event_tambahItemKeyPressed

    private void hapusItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusItemActionPerformed
        hapusData();
    }//GEN-LAST:event_hapusItemActionPerformed

    private void cbkodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbkodeActionPerformed
        kodeSparepart();
    }//GEN-LAST:event_cbkodeActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        cariKode();
        tampilStok();
        tampilKlik();
    }//GEN-LAST:event_jTable1MouseClicked

    private void batalNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalNotaActionPerformed
        // TODO add your handling code here:
        kosong();
        txplg.setText("");
        cbplg.setSelectedItem("Pelanggan");
        txmekanik.setText("");
        cbmekanik.setSelectedItem("Mekanik");
    }//GEN-LAST:event_batalNotaActionPerformed

    private void txBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txBayarKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            int bayar=Integer.parseInt(txBayar.getText());
            int total=Integer.parseInt(txTotal.getText());
            if (bayar>=total){
                int kembali=bayar-total;
                txKembalian.setText(String.valueOf(kembali));
                simpanNota();
                kosong();
            }else{
                JOptionPane.showMessageDialog(null, "Uang Anda Kurang");
                txBayar.requestFocus();
                txBayar.setText("");
            }

        }
    }//GEN-LAST:event_txBayarKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton batalNota;
    private javax.swing.JComboBox cbkode;
    private javax.swing.JComboBox cbmekanik;
    private javax.swing.JComboBox<String> cbplg;
    private javax.swing.JButton hapusItem;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton tambahItem;
    private javax.swing.JTextField txBayar;
    private javax.swing.JTextField txDateTime;
    private javax.swing.JTextField txHarga;
    private javax.swing.JTextField txKembalian;
    private javax.swing.JTextField txNo;
    private javax.swing.JTextField txStok;
    private javax.swing.JTextField txTotal;
    private javax.swing.JTextField txjumlah;
    private javax.swing.JTextField txmekanik;
    private javax.swing.JTextField txongkos;
    private javax.swing.JTextField txplg;
    // End of variables declaration//GEN-END:variables
}
