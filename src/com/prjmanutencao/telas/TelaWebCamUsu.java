/*
 * The MIT License
 *
 * Copyright 2025 fabio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.prjmanutencao.telas;

import com.prjmanutencao.dal.ModuloConexao;
import static com.prjmanutencao.telas.TelaUsuarios.ent2;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;
import static com.prjmanutencao.telas.TelaUsuarios.txtNomDir;
import static com.prjmanutencao.telas.TelaUsuarios.lblFotUsu;
import static com.prjmanutencao.telas.TelaUsuarios.btnAltFot;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Realiza a captura de imagens.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaWebCamUsu extends javax.swing.JFrame {

    private Dimension dimensao_defaul;
    private Webcam WEBCAM;
    boolean executando = true;

    public static byte[] BYTES_IMAGEM2 = null;

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Construtor `TelaWebCamUsu`. Responsável por inicializar a interface
     * gráfica, configurar o ícone da janela, estabelecer a conexão com o banco
     * de dados e preparar os componentes relacionados à funcionalidade da
     * webcam.
     */
    public TelaWebCamUsu() {
        initComponents();
        setIcon();
        conexao = ModuloConexao.conector();
        btnCapCam.setEnabled(false);
        btnParCam.setEnabled(false);
        Iniciar();

    }

    /**
     * Método `iniciar_camera`. Este método inicializa e abre a webcam,
     * configura os elementos da interface gráfica relacionados à câmera e
     * inicia o fluxo de vídeo para exibição em tempo real no componente
     * associado. A execução ocorre em uma thread separada para garantir que a
     * interface gráfica permaneça responsiva.
     *
     * Funcionalidades: - **Execução em Thread Separada**: - Utiliza uma nova
     * thread para realizar as operações de inicialização da câmera, evitando
     * bloqueios na interface.
     *
     * - **Configuração Inicial da Câmera**: - Define `executando = true` para
     * indicar que a câmera está ativa. - Desabilita o botão de inicialização da
     * câmera (`btnIniCam`) para evitar múltiplas inicializações. - Exibe a
     * mensagem `"Iniciando..."` no componente `lblImaCam` enquanto a câmera é
     * configurada.
     *
     * - **Inicialização da Webcam**: - `Webcam.setDriver(new
     * WebcamDefaultDriver())`: Configura o driver padrão para a webcam. -
     * `WEBCAM = Webcam.getDefault()`: Seleciona a webcam padrão do sistema. -
     * `WEBCAM.open()`: Abre a câmera e aloca os recursos necessários para seu
     * funcionamento. - Limpa a mensagem em `lblImaCam` ao concluir a
     * inicialização.
     *
     * - **Início do Fluxo de Vídeo**: - Chama o método `Inicializa_video()`
     * para capturar e exibir o vídeo da câmera em tempo real.
     *
     * - **Atualização da Interface**: - Habilita os botões `btnCapCam`
     * (capturar imagem) e `btnParCam` (parar câmera) após a inicialização.
     *
     * Fluxo do Método: 1. Cria e executa uma thread separada para inicializar a
     * câmera. 2. Configura a câmera e os componentes da interface gráfica. 3.
     * Inicia o fluxo de vídeo. 4. Habilita os botões relevantes e exibe o
     * status adequado na interface.
     */
    private void iniciar_camera() {

        new Thread() {
            @Override
            public void run() {
                executando = true;
                btnIniCam.setEnabled(false);
                lblImaCam.setText("Iniciando...");
                Webcam.setDriver(new WebcamDefaultDriver());
                WEBCAM = Webcam.getDefault();
                WEBCAM.open();
                lblImaCam.setText("");
                Inicializa_video();
                btnCapCam.setEnabled(true);
                btnParCam.setEnabled(true);
            }
        }.start();

    }

    /**
     * Método `capturar_imagem`. Responsável por capturar uma imagem da webcam,
     * redimensioná-la, salvar em um diretório específico, converter para bytes
     * e exibi-la na interface gráfica. Também desliga a webcam após a captura.
     */
    private void capturar_imagem() {

        String caminho = "/../systemOs"; // Caminho relativo
        Random aleatorio = new Random();

        int valor = aleatorio.nextInt(300000) + 1;
        try {
            executando = false;
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            ImageIO.write(WEBCAM.getImage(), "JPG", buff);
            byte[] bytes = buff.toByteArray();

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            BufferedImage imagem = ImageIO.read(is);

            int Nova_Largura = 720, Nova_Altura = 480; //aqui se de a largura e altura da imagem pix
            BufferedImage new_img = new BufferedImage(Nova_Largura, Nova_Altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = new_img.createGraphics();
            g.drawImage(imagem, 0, 0, Nova_Largura, Nova_Altura, null);
            txtNomDir.setText(Integer.toString(valor));

            // Garante que o diretório escolhido exista
            Path caminhoDiretorio = Paths.get(caminho);

            // Define o caminho e nome do arquivo
            File arquivoImagem = new File(caminhoDiretorio.toFile(), valor + ".jpg");

            ImageIO.write(new_img, "JPG", arquivoImagem);

            ent2 = arquivoImagem.toString();

            ByteArrayOutputStream buff2 = new ByteArrayOutputStream();
            ImageIO.write(new_img, "JPG", buff2);

            BYTES_IMAGEM2 = buff2.toByteArray(); // AQUI A IMAGEM ESTÁ CONVERTIDA EM BYTES NO TAMANHO PERSONALIZADO

            ImageIcon icon = new ImageIcon(BYTES_IMAGEM2);
            icon.setImage(icon.getImage().getScaledInstance(lblFotUsu.getWidth(), lblFotUsu.getHeight(), Image.SCALE_SMOOTH));
            lblFotUsu.setIcon(icon);

            WEBCAM.close();
            btnAltFot.setEnabled(true);

            parar_camera();
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    /**
     * Método `parar_camera`. Responsável por desligar a webcam, redefinir o
     * estado dos botões e limpar o rótulo de exibição da câmera.
     */
    private void parar_camera() {

        new Thread() {
            @Override
            public void run() {

                executando = false;
                WEBCAM.close();
                lblImaCam.setIcon(null);
                btnIniCam.setEnabled(true);
                btnCapCam.setEnabled(false);
                btnParCam.setEnabled(false);
            }
        }.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        lblImaCam = new javax.swing.JLabel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        btnIniCam = new javax.swing.JButton();
        btnParCam = new javax.swing.JButton();
        btnCapCam = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Webcam System O.S");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        jLayeredPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51)));

        lblImaCam.setBackground(new java.awt.Color(102, 102, 102));
        lblImaCam.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        lblImaCam.setForeground(new java.awt.Color(153, 255, 0));
        lblImaCam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImaCam.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        lblImaCam.setOpaque(true);
        jLayeredPane1.add(lblImaCam);
        lblImaCam.setBounds(10, 10, 280, 280);

        getContentPane().add(jLayeredPane1);
        jLayeredPane1.setBounds(100, 0, 300, 300);

        jLayeredPane2.setBackground(new java.awt.Color(153, 153, 153));
        jLayeredPane2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51)));
        jLayeredPane2.setOpaque(true);

        btnIniCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/Start.png"))); // NOI18N
        btnIniCam.setToolTipText("Iniciar WebCam");
        btnIniCam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniCamActionPerformed(evt);
            }
        });
        jLayeredPane2.add(btnIniCam);
        btnIniCam.setBounds(18, 10, 64, 64);

        btnParCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/Stop.png"))); // NOI18N
        btnParCam.setToolTipText("Parar WebCam");
        btnParCam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnParCam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParCamActionPerformed(evt);
            }
        });
        jLayeredPane2.add(btnParCam);
        btnParCam.setBounds(18, 90, 64, 64);

        btnCapCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/Captura.png"))); // NOI18N
        btnCapCam.setToolTipText("Captura");
        btnCapCam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapCam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapCamActionPerformed(evt);
            }
        });
        jLayeredPane2.add(btnCapCam);
        btnCapCam.setBounds(18, 170, 64, 64);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/webcam.png"))); // NOI18N
        jLabel1.setToolTipText("Webcam System");
        jLayeredPane2.add(jLabel1);
        jLabel1.setBounds(26, 240, 50, 50);

        getContentPane().add(jLayeredPane2);
        jLayeredPane2.setBounds(0, 0, 100, 300);

        setSize(new java.awt.Dimension(416, 339));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método `btnIniCamActionPerformed`. Manipula o evento de clique no botão
     * "Iniciar Câmera". Invoca o método `iniciar_camera()` para ativar a webcam
     * e iniciar seu funcionamento.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnIniCamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniCamActionPerformed

        iniciar_camera();

    }//GEN-LAST:event_btnIniCamActionPerformed

    /**
     * Método `btnCapCamActionPerformed`. Manipula o evento de clique no botão
     * "Capturar Imagem". Invoca o método `capturar_imagem()` para capturar uma
     * foto da webcam, redimensioná-la, salvá-la em um diretório específico e
     * exibi-la na interface.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnCapCamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapCamActionPerformed

        capturar_imagem();

    }//GEN-LAST:event_btnCapCamActionPerformed

    /**
     * Método `btnParCamActionPerformed`. Manipula o evento de clique no botão
     * "Parar Câmera". Invoca o método `parar_camera()` para desativar a webcam,
     * limpar o rótulo de exibição da câmera e redefinir o estado dos botões
     * relacionados.
     *
     * @param evt Evento acionado pelo clique no botão.
     */
    private void btnParCamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParCamActionPerformed

        parar_camera();
    }//GEN-LAST:event_btnParCamActionPerformed

    /**
     * Método `formWindowClosing`. Manipula o evento de fechamento da janela.
     * Garante que a webcam seja desligada corretamente ao fechar a janela,
     * liberando recursos e prevenindo problemas futuros.
     *
     * @param evt Evento acionado ao fechar a janela.
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (executando == true) {
            parar_camera();
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaWebCamUsu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaWebCamUsu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaWebCamUsu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaWebCamUsu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TelaWebCamUsu().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapCam;
    private javax.swing.JButton btnIniCam;
    private javax.swing.JButton btnParCam;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JLabel lblImaCam;
    // End of variables declaration//GEN-END:variables

    /**
     * Método `Iniciar`. Configura e inicializa a webcam. Determina a resolução
     * padrão, identifica a última webcam conectada ao sistema (se houver
     * múltiplas) e lista as resoluções suportadas pela câmera. Em caso de erro,
     * exibe uma mensagem de exceção ao usuário.
     */
    private void Iniciar() {
        try {
//            dimensao_defaul = new Dimension(176, 144); //UTILIZO A DIMENSÃO DE MINHA PREFERÊNCIA
            dimensao_defaul = WebcamResolution.VGA.getSize();//UTILIZA A DIMENSÃO PADRÃO DA WEBCAM
//            WEBCAM = Webcam.getDefault(); //UTILIZA A WEBCAM PADRÃO DO COMPUTADOR            
            List list = Webcam.getWebcams();
            for (int i = 0; i < list.size(); i++) {
                WEBCAM = (Webcam) list.get(i); //UTILIZA A ÚLTIMA WEBCAM ENCONTRADA, SE EXISTIR MAIS DE UMA PEGA A ÚLTIMA
            }
            WEBCAM.setViewSize(dimensao_defaul);

            for (Dimension dimensao : WEBCAM.getViewSizes()) { // AQUI EU CONSIGO SABER QUAIS SÃO RESOLUÇÕES SUPORTADA PELA WEBCAM
                System.out.println("Largura: " + dimensao.getWidth() + "  Altura: " + dimensao.getHeight());
            }

        } catch (WebcamException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `Inicializa_video`. Responsável por capturar imagens contínuas da
     * webcam em tempo real e exibi-las na interface gráfica. Utiliza uma thread
     * dedicada para manter o feed de vídeo enquanto a webcam está ativa.
     */
    private void Inicializa_video() {
        new Thread() {
            @Override
            public void run() {
                while (true && executando) {
                    try {
                        Image imagem = WEBCAM.getImage();
                        ImageIcon icon = new ImageIcon(imagem);
                        icon.setImage(icon.getImage().getScaledInstance(lblImaCam.getWidth(), lblImaCam.getHeight(), Image.SCALE_SMOOTH));
                        lblImaCam.setIcon(icon);
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            }
        }.start();
    }

    /**
     * Método responsável por definir o ícone principal da aplicação. Utiliza a
     * classe Toolkit para carregar a imagem do recurso especificado.
     */
    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/prjmanutencao/icones/iconPrincipal.png")));
    }
}
