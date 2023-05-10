import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VideoPlayer extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private EmbeddedMediaPlayer mediaPlayer;
    private Canvas videoSurface;

    public VideoPlayer() {
        setTitle("Video Player");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        videoSurface = new Canvas();
        videoSurface.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        contentPane.add(videoSurface, BorderLayout.CENTER);

        EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        mediaPlayer = mediaPlayerComponent.getMediaPlayer();
        mediaPlayer.setVideoSurface(mediaPlayerComponent.getVideoSurface());

        JPanel controlsPane = new JPanel();
        contentPane.add(controlsPane, BorderLayout.SOUTH);

        JButton rewindButton = new JButton("Rewind");
        rewindButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.skip(-10000);
            }
        });
        controlsPane.add(rewindButton);

        JButton playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButton.setText("Play");
                } else {
                    mediaPlayer.play();
                    playButton.setText("Pause");
                }
            }
        });
        controlsPane.add(playButton);

        JButton fastForwardButton = new JButton("Fast Forward");
        fastForwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.skip(10000);
            }
        });
        controlsPane.add(fastForwardButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void playVideo(String filename) {
        mediaPlayer.playMedia(filename);
    }

    private String selectVideoFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Video files", "mp4", "avi", "mkv", "wmv", "mov");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return file.getAbsolutePath();
        }
        return null;
    }

    public static void main(String[] args) {
        boolean found = new NativeDiscovery().discover();
        if (!found) {
            JOptionPane.showMessageDialog(null, "Unable to locate VLC libraries", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        VideoPlayer player = new VideoPlayer();
        String filename = player.selectVideoFile();
        if (filename != null) {
            player.play
