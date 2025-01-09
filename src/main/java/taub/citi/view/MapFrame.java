package taub.citi.view;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;
import taub.citi.MapController;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

    public class MapFrame extends JFrame
    {
        final JXMapViewer mapViewer = new JXMapViewer();
        MapController controller;

        public MapFrame()
        {
            // Create a TileFactoryInfo for OpenStreetMap
            TileFactoryInfo info = new OSMTileFactoryInfo();
            DefaultTileFactory tileFactory = new DefaultTileFactory(info);

            // Setup local file cache
            File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
            tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));
            mapViewer.setTileFactory(tileFactory);
            GeoPosition newyork = new GeoPosition(40.82, -73.9);
            // Set the focus
            mapViewer.setZoom(7);
            mapViewer.setAddressLocation(newyork);
            // Add interactions
            MouseInputListener mia = new PanMouseInputListener(mapViewer);
            mapViewer.addMouseListener(mia);
            mapViewer.addMouseMotionListener(mia);

            mapViewer.addMouseListener(new CenterMapListener(mapViewer));

            mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

            mapViewer.addKeyListener(new PanKeyListener(mapViewer));

            // Add a selection painter
            SelectionAdapter sa = new SelectionAdapter(mapViewer);
            SelectionPainter sp = new SelectionPainter(sa);
            mapViewer.addMouseListener(sa);
            mapViewer.addMouseMotionListener(sa);
            mapViewer.setOverlayPainter(sp);

            // Display the viewer in a JFrame
            final JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            String text = "Use left mouse button to pan, mouse wheel to zoom and right mouse to select";
            frame.add(new JLabel(text), BorderLayout.NORTH);
            frame.add(mapViewer);
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            addToolbar(frame, mapViewer);

            frame.setVisible(true);
            controller = new MapController(mapViewer);

            mapViewer.addPropertyChangeListener("zoom", new PropertyChangeListener()
            {
                @Override
                public void propertyChange(PropertyChangeEvent evt)
                {
                    updateWindowTitle(frame, mapViewer);
                }
            });

            mapViewer.addPropertyChangeListener("center", new PropertyChangeListener()
            {
                @Override
                public void propertyChange(PropertyChangeEvent evt)
                {
                    updateWindowTitle(frame, mapViewer);
                }
            });

            updateWindowTitle(frame, mapViewer);


            mapViewer.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    controller.whenMouseClicked(x, y);
                }
            });


        }

        private void addToolbar(Frame frame, JXMapViewer mapViewer)
        {
            // create a toolbar
            JToolBar tb = new JToolBar();

            // create a panel
            JPanel p = new JPanel();

            // create new buttons
            JButton mapButton = new JButton("Map");
            JButton clearButton = new JButton("Clear");
            p.add(mapButton);
            p.add(clearButton);
            tb.add(p);
            frame.add(tb, BorderLayout.SOUTH);

            clearButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    controller.clearScreen();
                }
            });

            mapButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (controller.getWaypointsSize() != 2)
                    {
                        JOptionPane.showMessageDialog(null, "You must have two points to create a route.\n"
                                +
                                "Either click to add more points, or press clear and start again.");
                    } else
                    {
                        controller.doPoints();
                    }
                }
            });
        }

        protected void updateWindowTitle(JFrame frame, JXMapViewer mapViewer)
        {
            double lat = mapViewer.getCenterPosition().getLatitude();
            double lon = mapViewer.getCenterPosition().getLongitude();
            int zoom = mapViewer.getZoom();
            frame.setTitle(String.format("CitiBike Map (%.2f / %.2f) - Zoom: %d", lat, lon, zoom));
        }
    }


