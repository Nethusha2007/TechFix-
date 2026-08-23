package com.techfix.app.data;

import com.techfix.app.R;
import com.techfix.app.model.Branch;
import com.techfix.app.model.DeviceCategory;
import com.techfix.app.model.ServiceItem;
import com.techfix.app.model.SparePart;
import com.techfix.app.model.Technician;
import com.techfix.app.model.TrackStep;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory reference/catalog data for lists that don't need persistence
 * (branches, services, device categories, technicians, spare parts, tracking steps).
 * Transactional data — appointments, payments, notifications — comes from the DB.
 */
public final class SampleData {

    private SampleData() {
    }

    // --------------------------------------------------------------- branches

    public static List<Branch> branches() {
        List<Branch> list = new ArrayList<>();
        list.add(new Branch(1, "TechFix Colombo", "No. 24, Galle Road, Colombo 03",
                "Colombo", "2.4 km", "Open · Closes 8:00 PM", "+94 11 234 5678", 6.9034, 79.8564));
        list.add(new Branch(2, "TechFix Kandy", "No. 112, Peradeniya Road, Kandy",
                "Kandy", "115 km", "Open · Closes 7:00 PM", "+94 81 223 4567", 7.2916, 80.6300));
        list.add(new Branch(3, "TechFix Galle", "No. 8, Wackwella Road, Galle",
                "Galle", "119 km", "Open · Closes 7:30 PM", "+94 91 224 5678", 6.0535, 80.2210));
        list.add(new Branch(4, "TechFix Negombo", "No. 56, Colombo Road, Negombo",
                "Negombo", "37 km", "Closed · Opens 9:00 AM", "+94 31 227 6543", 7.2086, 79.8380));
        return list;
    }

    // --------------------------------------------------------------- services

    public static List<ServiceItem> services() {
        List<ServiceItem> list = new ArrayList<>();
        list.add(new ServiceItem(1, "Screen Replacement", "From LKR 4,500",
                R.drawable.ic_screen, "Cracked or unresponsive display replacement with genuine parts."));
        list.add(new ServiceItem(2, "Battery Replacement", "From LKR 3,200",
                R.drawable.ic_battery, "Restore battery life with a certified replacement battery."));
        list.add(new ServiceItem(3, "Charging Port Repair", "From LKR 2,800",
                R.drawable.ic_charging, "Fix loose or non-working charging ports."));
        list.add(new ServiceItem(4, "Software Repair", "From LKR 2,500",
                R.drawable.ic_software, "OS reinstall, virus removal and performance tuning."));
        list.add(new ServiceItem(5, "Water Damage Repair", "From LKR 6,500",
                R.drawable.ic_water, "Liquid-damage diagnostics and board-level cleaning."));
        list.add(new ServiceItem(6, "Data Recovery", "From LKR 5,000",
                R.drawable.ic_data, "Recover lost photos, documents and files safely."));
        return list;
    }

    // ------------------------------------------------------- device categories

    public static List<DeviceCategory> deviceCategories() {
        List<DeviceCategory> list = new ArrayList<>();
        list.add(new DeviceCategory(1, "Mobile Phone", R.drawable.ic_mobile));
        list.add(new DeviceCategory(2, "Laptop", R.drawable.ic_laptop));
        list.add(new DeviceCategory(3, "Desktop PC", R.drawable.ic_desktop));
        list.add(new DeviceCategory(4, "Tablet", R.drawable.ic_tablet));
        list.add(new DeviceCategory(5, "MacBook", R.drawable.ic_macbook));
        list.add(new DeviceCategory(6, "Accessories", R.drawable.ic_accessories));
        return list;
    }

    /** Brand options shown in Book-Repair Step 2, keyed by category name. */
    public static List<String> deviceBrands(String category) {
        List<String> list = new ArrayList<>();
        if (category == null) category = "";
        switch (category) {
            case "Laptop":
                list.add("Dell");
                list.add("HP");
                list.add("Lenovo");
                list.add("Asus");
                list.add("Acer");
                list.add("MSI");
                break;
            case "Desktop PC":
                list.add("Dell");
                list.add("HP");
                list.add("Lenovo");
                list.add("Custom Build");
                break;
            case "Tablet":
                list.add("Apple iPad");
                list.add("Samsung");
                list.add("Huawei");
                list.add("Lenovo");
                break;
            case "MacBook":
                list.add("MacBook Air");
                list.add("MacBook Pro 13\"");
                list.add("MacBook Pro 14\"");
                list.add("MacBook Pro 16\"");
                break;
            case "Accessories":
                list.add("Charger");
                list.add("Headphones");
                list.add("Keyboard");
                list.add("Mouse");
                list.add("Smart Watch");
                break;
            case "Mobile Phone":
            default:
                list.add("Apple iPhone");
                list.add("Samsung");
                list.add("Google Pixel");
                list.add("Xiaomi");
                list.add("Huawei");
                list.add("OnePlus");
                break;
        }
        return list;
    }

    // ------------------------------------------------------------ technicians

    public static List<Technician> technicians() {
        List<Technician> list = new ArrayList<>();
        list.add(new Technician(1, "Nuwan Perera", "Mobile Repair Specialist", "Colombo", true));
        list.add(new Technician(2, "Kasun Fernando", "Laptop & Mac Specialist", "Kandy", false));
        list.add(new Technician(3, "Ishara Jayasuriya", "Hardware Technician", "Galle", true));
        list.add(new Technician(4, "Dilan Rathnayake", "Software Specialist", "Colombo", true));
        list.add(new Technician(5, "Sandun Wickramasinghe", "Data Recovery Expert", "Colombo", false));
        list.add(new Technician(6, "Tharindu Bandara", "Board-Level Technician", "Negombo", true));
        return list;
    }

    /** Just the technician names (for the assign-technician picker). */
    public static List<String> technicianNames() {
        List<String> names = new ArrayList<>();
        for (Technician t : technicians()) names.add(t.name);
        return names;
    }

    // ----------------------------------------------------------- spare parts

    /** Branch spare-parts inventory shown on the admin Spare Parts screen. */
    public static List<SparePart> spareParts() {
        List<SparePart> list = new ArrayList<>();
        list.add(new SparePart(1, "iPhone 13 OLED Screen", "Screens", "LKR 18,500", 12));
        list.add(new SparePart(2, "Samsung S21 AMOLED Screen", "Screens", "LKR 16,200", 4));
        list.add(new SparePart(3, "iPhone 12 Battery", "Batteries", "LKR 4,800", 25));
        list.add(new SparePart(4, "MacBook Air Battery", "Batteries", "LKR 12,500", 0));
        list.add(new SparePart(5, "USB-C Charging Port", "Charging", "LKR 2,200", 40));
        list.add(new SparePart(6, "Lightning Charging Flex", "Charging", "LKR 2,600", 3));
        list.add(new SparePart(7, "Laptop SSD 512GB", "Storage", "LKR 14,900", 8));
        list.add(new SparePart(8, "Laptop RAM 8GB DDR4", "Memory", "LKR 6,400", 15));
        list.add(new SparePart(9, "Phone Back Glass Panel", "Housing", "LKR 3,500", 0));
        list.add(new SparePart(10, "Laptop Cooling Fan", "Cooling", "LKR 3,900", 6));
        return list;
    }

    // -------------------------------------------------------- tracking steps

    private static final String[] STEP_TITLES = {
            "Booking Confirmed",
            "Device Received",
            "Diagnosis Started",
            "Diagnosis Complete",
            "Repair In Progress",
            "Quality Check",
            "Ready for Collection",
            "Repair Completed"
    };

    private static final String[] STEP_TIMES = {
            "18 May 2026 · 09:15 AM",
            "18 May 2026 · 10:30 AM",
            "18 May 2026 · 11:45 AM",
            "18 May 2026 · 01:20 PM",
            "18 May 2026 · 03:30 PM",
            "19 May 2026 · 10:00 AM",
            "19 May 2026 · 12:15 PM",
            "19 May 2026 · 02:00 PM"
    };

    /**
     * Builds the 8-step tracking timeline for an appointment.
     * Steps before {@code current} are DONE, the step at {@code current} is CURRENT,
     * and later steps are PENDING (with no timestamp shown).
     */
    public static List<TrackStep> trackSteps(int current) {
        List<TrackStep> list = new ArrayList<>();
        for (int i = 0; i < STEP_TITLES.length; i++) {
            int state;
            String time;
            if (i < current) {
                state = TrackStep.DONE;
                time = STEP_TIMES[i];
            } else if (i == current) {
                state = TrackStep.CURRENT;
                time = STEP_TIMES[i];
            } else {
                state = TrackStep.PENDING;
                time = "";
            }
            list.add(new TrackStep(STEP_TITLES[i], time, state));
        }
        return list;
    }

    public static String stepTitle(int index) {
        if (index < 0) index = 0;
        if (index >= STEP_TITLES.length) index = STEP_TITLES.length - 1;
        return STEP_TITLES[index];
    }

    // ----------------------------------------------------------- repair gallery

    /** Caption labels for the before/after repair photo gallery (placeholder images). */
    public static List<String> galleryCaptions() {
        List<String> list = new ArrayList<>();
        list.add("Before · Cracked screen");
        list.add("Before · Water damage");
        list.add("Diagnosis · Board inspection");
        list.add("Repair · Part replacement");
        list.add("After · Screen restored");
        list.add("After · Quality tested");
        return list;
    }
}
