package Client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import protocol.*;
import se.lth.cs.fakecamera.Axis211A;

public class GUI extends JFrame {
	private ImagePanel[] imagePanel;
	private JLabel[] delays;
	private JButton movie, idle, sync, async;
	private byte[] jpeg = new byte[Axis211A.IMAGE_BUFFER_SIZE];
	private JTextArea text;
	private int imSize;
	private Container imShow,imLabels, imPanels, buttons;

	public GUI(Buttons buttonHandler, final MonitorBuffer mon, int imSize) {
		super();
		this.imSize = imSize;
		buttons = new Container();
		imPanels = new Container();
		imLabels = new Container();
		imShow = new Container();
		buttons.setLayout(new GridLayout(0,4));
		imPanels.setLayout(new GridLayout(0,imSize));
		imLabels.setLayout(new GridLayout(0, imSize));
		imShow.setLayout(new BorderLayout(2,0));
		imagePanel = new ImagePanel[imSize];
		delays = new JLabel[imSize];
		for(int i=0; i<imSize; i++){
			imagePanel[i] = new ImagePanel();
			delays[i] = new JLabel("0 ms");
			imPanels.add(imagePanel[i]);
			imLabels.add(delays[i]);
		}
		imShow.add(imPanels, BorderLayout.NORTH);
		imShow.add(imLabels, BorderLayout.CENTER);
		text = new JTextArea();
		idle = new JButton("Idle");
		movie = new JButton("Movie");
		sync = new JButton("Synchronous");
		async = new JButton("Asynchronous");
		idle.addActionListener(buttonHandler.getIdleListener());
		movie.addActionListener(buttonHandler.getMovieListener());
		sync.addActionListener(buttonHandler.getSyncListener());
		async.addActionListener(buttonHandler.getAsyncListener());
		buttons.add(movie);
		buttons.add(idle);
		buttons.add(sync);
		buttons.add(async);
		this.getContentPane().setLayout(new BorderLayout(0,imSize));
		this.getContentPane().add(imShow, BorderLayout.CENTER);
		this.getContentPane().add(buttons, BorderLayout.SOUTH);
		this.getContentPane().add(text,BorderLayout.NORTH);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		this.setSize(410, 410);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent arg0) {
				mon.setMode(DataPacket.CONCLOSE);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {e.printStackTrace();}
				System.exit(1);
			}
			public void windowOpened(WindowEvent arg0) {}
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}

		});
	}

	public void refreshImage(ImageContainer[] image) {
		for(int i=0; i<imSize; i++){
			if(image[i] != null){ 
				byte[] im = image[i].getImage();
				imagePanel[i].refresh(im);
				delays[i].setText(image[i].getDelay() + " ms");
				}
			}
		this.pack();
	}
	
	public void setSync(boolean sync){
		if(sync){
			text.setText("Synchronous");
		}
		else{
			text.setText("Asynchronous");
		}
	}
	
	private class ImagePanel extends JPanel {
		private ImageIcon icon;

		public ImagePanel() {
			super();
			icon = new ImageIcon();
			JLabel label = new JLabel(icon);
			add(label, BorderLayout.CENTER);
			this.setSize(200, 200);
		}

		public void refresh(byte[] data) {
			Image theImage = getToolkit().createImage(data);
			getToolkit().prepareImage(theImage, -1, -1, null);
			icon.setImage(theImage);
			icon.paintIcon(this, this.getGraphics(), 5, 5);
		}
	}
}
