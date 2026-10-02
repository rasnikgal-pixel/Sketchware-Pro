package mod.jbk.build;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import pro.sketchware.xml.XmlBuilder;

/**
 * Merges elements from local library manifests (android:name-tagged providers,
 * meta-data, activities, services, receivers and uses-permission) into the
 * project's AndroidManifest.xml.
 *
 * Fixes issue #1971: libraries that need a &lt;provider&gt; or &lt;meta-data&gt;
 * (e.g. RuStore Pay SDK, Firebase, Google Play Services) were silently dropped
 * because their manifests were never merged.
 */
public class LocalLibraryManifestMerger {

    private static final String TAG = "LibManifestMerger";

    /** Merges all manifests found at the given paths. */
    public static void merge(XmlBuilder manifestTag, XmlBuilder applicationTag, List<String> manifestPaths) {
        if (manifestPaths == null || manifestPaths.isEmpty()) return;

        Set<String> addedPermissions = new HashSet<>();
        Set<String> addedAppChildNames = new HashSet<>();

        for (String path : manifestPaths) {
            try {
                File file = new File(path);
                if (!file.exists()) continue;

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(false);
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(file);
                doc.getDocumentElement().normalize();

                Element root = doc.getDocumentElement();
                if (root == null) continue;

                // <uses-permission> -> <manifest>
                NodeList permissions = root.getElementsByTagName("uses-permission");
                for (int i = 0; i < permissions.getLength(); i++) {
                    Element el = (Element) permissions.item(i);
                    String name = el.getAttribute("android:name");
                    if (name.isEmpty()) name = el.getAttributeNS("http://schemas.android.com/apk/res/android", "name");
                    if (!name.isEmpty() && addedPermissions.add(name)) {
                        XmlBuilder p = new XmlBuilder("uses-permission");
                        p.addAttribute("android", "name", name);
                        manifestTag.addChildNode(p);
                    }
                }

                // <application> children -> <application>
                NodeList applications = root.getElementsByTagName("application");
                for (int i = 0; i < applications.getLength(); i++) {
                    Element appEl = (Element) applications.item(i);
                    NodeList children = appEl.getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        Node node = children.item(j);
                        if (node.getNodeType() != Node.ELEMENT_NODE) continue;
                        Element child = (Element) node;
                        String tagName = child.getTagName();
                        if (!isSupportedApplicationChild(tagName)) continue;

                        String name = child.getAttribute("android:name");
                        if (name.isEmpty()) name = child.getAttributeNS("http://schemas.android.com/apk/res/android", "name");
                        String key = tagName + "#" + name;
                        if (!name.isEmpty() && !addedAppChildNames.add(key)) continue;
                        if (name.isEmpty()) continue;

                        XmlBuilder built = buildFromElement(child);
                        if (built != null) applicationTag.addChildNode(built);
                    }
                }
            } catch (Throwable t) {
                Log.w(TAG, "Failed to merge manifest: " + path, t);
            }
        }
    }

    private static boolean isSupportedApplicationChild(String tag) {
        switch (tag) {
            case "provider":
            case "meta-data":
            case "activity":
            case "service":
            case "receiver":
                return true;
            default:
                return false;
        }
    }

    private static XmlBuilder buildFromElement(Element el) {
        XmlBuilder xml = new XmlBuilder(el.getTagName());
        NamedNodeMap attrs = el.getAttributes();
        for (int k = 0; k < attrs.getLength(); k++) {
            Node a = attrs.item(k);
            String name = a.getNodeName();
            String value = a.getNodeValue();
            if (name.startsWith("android:")) {
                xml.addAttribute("android", name.substring("android:".length()), value);
            } else if (name.startsWith("xmlns:")) {
                // Skip namespace declarations
            } else {
                xml.addAttribute("", name, value);
            }
        }
        // Recursively copy children (for <meta-data> inside <provider>, <intent-filter> inside <activity>)
        NodeList kids = el.getChildNodes();
        for (int k = 0; k < kids.getLength(); k++) {
            Node n = kids.item(k);
            if (n.getNodeType() != Node.ELEMENT_NODE) continue;
            XmlBuilder childXml = buildFromElement((Element) n);
            if (childXml != null) xml.addChildNode(childXml);
        }
        return xml;
    }
}
