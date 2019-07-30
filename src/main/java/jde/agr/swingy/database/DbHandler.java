package jde.agr.swingy.database;

import jde.agr.swingy.Utility.EArtifact;
import jde.agr.swingy.Utility.EType;
import jde.agr.swingy.Utility.Logger;
import jde.agr.swingy.controller.CharacterFactory;
import jde.agr.swingy.model.artifacts.Armor;
import jde.agr.swingy.model.artifacts.Artifact;
import jde.agr.swingy.model.artifacts.Helm;
import jde.agr.swingy.model.artifacts.Weapon;
import jde.agr.swingy.model.characters.*;
import jde.agr.swingy.model.characters.Character;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jde.agr.swingy.Utility.Global.*;

public class DbHandler {

    private static final String DB_NAME = "swingy.db";
    private static final String HEROES_TABLE = "heroes";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_XP = "xp";
    private static final String KEY_ATTACK = "attack";
    private static final String KEY_DEFENSE = "defense";
    private static final String KEY_HP = "hp";
    private static final String KEY_WEAPON = "weapon";
    private static final String KEY_ARMOR = "armor";
    private static final String KEY_HELM = "helm";
    private static final String CREATE_HEROES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + HEROES_TABLE + " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                    KEY_NAME + " TEXT, " + KEY_TYPE + " TEXT, " + KEY_LEVEL + " INTEGER, " +
                    KEY_XP + " INTEGER, " + KEY_ATTACK + " INTEGER, " + KEY_DEFENSE + " INTEGER, " +
                    KEY_HP + " INTEGER, " + KEY_WEAPON + " BLOB, " + KEY_ARMOR + " BLOB, " +
                    KEY_HELM + " BLOB)";
    private static final String INSERT_HERO =
            "INSERT INTO " + HEROES_TABLE + " (" +  KEY_NAME + "," + KEY_TYPE + "," +
                    KEY_LEVEL + "," + KEY_XP + "," + KEY_ATTACK + "," + KEY_DEFENSE + "," +
                    KEY_HP + "," + KEY_WEAPON + "," + KEY_ARMOR + "," + KEY_HELM +
                    ") VALUES(?,?,?,?,?,?,?,?,?,?)";
    private static final String GET_HEROES_TABLE = "SELECT * FROM " + HEROES_TABLE;
    private static final String GET_HERO_DATA = "SELECT * FROM " + HEROES_TABLE + " WHERE " + KEY_NAME + " = ?";
    private static final String UPDATE_HERO_DATA = "UPDATE " + HEROES_TABLE + " SET " +
            KEY_LEVEL + " = ?, " + KEY_XP + " = ?, " + KEY_ATTACK + " = ?," + KEY_DEFENSE + " = ?," +
            KEY_HP + " = ?, " + KEY_WEAPON + " = ?," + KEY_ARMOR + " = ?," + KEY_HELM + " = ? WHERE " +
            KEY_NAME + " = ?";
    private static final String DELETE_HERO_DATA = "DELETE from " + HEROES_TABLE + " WHERE " + KEY_NAME + " = ?";

    private static final String SQLITE_DRIVER = "org.sqlite.JDBC";
    private static final String SQLITE_URL = "jdbc:sqlite:" + DB_NAME;

    private Connection connection;
    private Statement st;
    private static DbHandler dbHandler;

    private byte[] byteArray;
    private ByteArrayInputStream in;
    private PreparedStatement pst;
    private ResultSet rs;

    public static synchronized DbHandler getInstance() {
        if (dbHandler == null) {
            dbHandler = new DbHandler();
        }
        return (dbHandler);
    }

    private Connection connectToDB() {
        try {
            Class.forName(SQLITE_DRIVER);
            connection = DriverManager.getConnection(SQLITE_URL);
            st = connection.createStatement();
            st.executeUpdate(CREATE_HEROES_TABLE);
        } catch (SQLException | ClassNotFoundException e) {
            Logger.print("SQL exception - connectToDB(): " + e.getMessage());
            System.exit(0);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return (connection);
    }

    private ByteArrayInputStream serializeObject(Hero hero, EArtifact a) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        switch (a) {
            case WEAPON:
                os.writeObject(hero.getWeapon());
                break ;
            case ARMOR:
                os.writeObject(hero.getArmor());
                break ;
            case HELM:
                os.writeObject(hero.getHelm());
                break ;
            default:
                return (null);
        }
        byteArray = out.toByteArray();
        in = new ByteArrayInputStream(byteArray);
        return (in);
    }

    private Object deserializeObject(ResultSet rs, String key) throws SQLException, IOException, ClassNotFoundException {
        byteArray = rs.getBytes(key);
        in = new ByteArrayInputStream(byteArray);
        ObjectInputStream is = new ObjectInputStream(in);
        return (is.readObject());
    }

    private boolean isDuplicateHero(Connection connection, Hero hero) throws SQLException {
        st = connection.createStatement();
        rs = st.executeQuery(GET_HEROES_TABLE);
        while (rs.next()) {
            if (hero.getName().equals(rs.getString(KEY_NAME))) {
                return (true);
            }
        }
        return (false);
    }

    public void insertHero(Hero hero) {
        try {
            connection = this.connectToDB();
            if (isDuplicateHero(connection, hero)) {
                Logger.print("Db: This Hero already exists");
            } else {
                pst = connection.prepareStatement(INSERT_HERO);
                pst.setString(1, hero.getName());
                pst.setString(2, hero.getType());
                pst.setInt(3, hero.getLevel());
                pst.setInt(4, hero.getXp());
                pst.setInt(5, hero.getAttack());
                pst.setInt(6, hero.getDefense());
                pst.setInt(7, hero.getHp());
                pst.setBinaryStream(8, serializeObject(hero, EArtifact.WEAPON), byteArray.length);
                pst.setBinaryStream(9, serializeObject(hero, EArtifact.ARMOR), byteArray.length);
                pst.setBinaryStream(10, serializeObject(hero, EArtifact.HELM), byteArray.length);
                pst.executeUpdate();
                Logger.print("Db: <" + hero.getName() + ">" + " created");
            }
        } catch (SQLException | IOException e) {
            Logger.print("SQL exception - connectToDB(): " + e.getMessage());
            System.exit(0);
        } finally {
            closeAll();
        }
    }

    public List<Hero> getDB() {
        try {
            List<Hero> heroList = new ArrayList<>();

            connection = this.connectToDB();
            st = connection.createStatement();
            rs = st.executeQuery(GET_HEROES_TABLE);
            while (rs.next()) {
                bIsHero = true;
                Hero hero = null;
                switch (rs.getString(KEY_TYPE)) {
                    case "Warrior":
                        hero = new Warrior();
                        break;
                    case "Thief":
                        hero = new Thief();
                        break;
                    case "Wizard":
                        hero = new Wizard();
                }
                assert hero != null;
                hero.setName(rs.getString(KEY_NAME));
                hero.setType(rs.getString(KEY_TYPE));
                hero.setLevel(rs.getInt(KEY_LEVEL));
                hero.setXp(rs.getInt(KEY_XP));
                hero.setAttack(rs.getInt(KEY_ATTACK));
                hero.setDefense(rs.getInt(KEY_DEFENSE));
                hero.setHp(rs.getInt(KEY_HP));
                hero.setWeapon((Weapon) deserializeObject(rs, KEY_WEAPON));
                hero.setArmor((Armor) deserializeObject(rs, KEY_ARMOR));
                hero.setHelm((Helm) deserializeObject(rs, KEY_HELM));
                heroList.add(hero);
            }
            return (heroList);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            Logger.print("SQL exception - connectToDB(): " + e.getMessage());
            System.exit(0);
        } finally {
            closeAll();
        }
        return (null);
    }

    public void printDB() {
        try {
            StringBuilder sb = new StringBuilder();
            connection = this.connectToDB();
            st = connection.createStatement();
            rs = st.executeQuery(GET_HEROES_TABLE);
            if (bIsGUI) {
                logTextArea.setText("");
            }
            nbHero = 0;
            while (rs.next()) {
                bIsHero = true;
                sb.append("Name: ").append(rs.getString(KEY_NAME)).append("\n")
                        .append("Type: ").append(rs.getString(KEY_TYPE)).append("\n")
                        .append("Level: ").append(rs.getInt(KEY_LEVEL)).append("\n")
                        .append("Experience: ").append(rs.getInt(KEY_XP)).append("\n")
                        .append("Attack: ").append(rs.getInt(KEY_ATTACK)).append("\n")
                        .append("Defense: ").append(rs.getInt(KEY_DEFENSE)).append("\n")
                        .append("Health: ").append(rs.getInt(KEY_HP)).append("\n")
                        .append("Weapon: ").append(((Artifact) deserializeObject(rs, KEY_WEAPON)).getName()).append("\n")
                        .append("Armor: ").append(((Artifact) deserializeObject(rs, KEY_ARMOR)).getName()).append("\n")
                        .append("Helm: ").append(((Artifact) deserializeObject(rs, KEY_HELM)).getName()).append("\n\n");
                nbHero += 1;
            }
            Logger.print(sb.toString());
        } catch (SQLException | ClassNotFoundException | IOException e) {
            Logger.print("SQL exception - connectToDB(): " + e.getMessage());
            System.exit(0);
        } finally {
            closeAll();
        }
    }

    public boolean isValidId(int id) {
        try {
            connection = this.connectToDB();
            st = connection.createStatement();
            rs = st.executeQuery(GET_HEROES_TABLE);

            while (rs.next()) {
                if (rs.getInt(KEY_ID) == id) {
                    return (true);
                }
            }
        } catch (SQLException e) {
            Logger.print("SQL exception - connectToDB(): " + e.getMessage());
            System.exit(0);
        } finally {
            closeAll();
        }
        return (false);
    }

    public void updateHero(Character hero) {
        try {
            connection = this.connectToDB();
            pst = connection.prepareStatement(UPDATE_HERO_DATA);
            pst.setInt(1, hero.getLevel());
            pst.setInt(2, ((Hero)hero).getXp());
            pst.setInt(3, hero.getAttack());
            pst.setInt(4, hero.getDefense());
            pst.setInt(5, hero.getHp());
            pst.setBinaryStream(6, serializeObject(((Hero)hero), EArtifact.WEAPON), byteArray.length);
            pst.setBinaryStream(7, serializeObject(((Hero)hero), EArtifact.ARMOR), byteArray.length);
            pst.setBinaryStream(8, serializeObject(((Hero)hero), EArtifact.HELM), byteArray.length);
            pst.setString(9, hero.getName());
            pst.executeUpdate();
        } catch (SQLException | IOException e) {
            Logger.print("SQL exception - connectToDB(): " + e.getMessage());
            System.exit(0);
        } finally {
            closeAll();
        }
    }

    public void deleteHero(String name) {
        try {
            connection = this.connectToDB();
            pst = connection.prepareStatement(DELETE_HERO_DATA);
            pst.setString(1, name);
            pst.executeUpdate();
        } catch (SQLException e) {
            Logger.print("SQL exception - connectToDB(): " + e.getMessage());
            System.exit(0);
        } finally {
            closeAll();
        }
    }

    public Hero getHeroData(String name) {
        Hero hero = null;

        try {
            connection = this.connectToDB();
            pst = connection.prepareStatement(GET_HERO_DATA);
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                switch (rs.getString(KEY_TYPE)) {
                    case "Warrior":
                        hero = (Hero) CharacterFactory.newHero(name, EType.WARRIOR);
                        break;
                    case "Thief":
                        hero = (Hero) CharacterFactory.newHero(name, EType.THIEF);
                        break;
                    case "Wizard":
                        hero = (Hero) CharacterFactory.newHero(name, EType.WIZARD);
                        break;
                }
                assert hero != null;
                hero.setLevel(rs.getInt(KEY_LEVEL));
                hero.setXp(rs.getInt(KEY_XP));
                hero.setAttack(rs.getInt(KEY_ATTACK));
                hero.setDefense(rs.getInt(KEY_DEFENSE));
                hero.setHp(rs.getInt(KEY_HP));
                hero.setWeapon((Weapon) deserializeObject(rs, KEY_WEAPON));
                hero.setArmor((Armor) deserializeObject(rs, KEY_ARMOR));
                hero.setHelm((Helm) deserializeObject(rs, KEY_HELM));
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            Logger.print("SQL exception - connectToDB(): " + e.getMessage());
            System.exit(0);
        } finally {
            closeAll();
        }
        return (hero);
    }

    private void closeAll() {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (st != null && !st.isClosed()) {
                st.close();
            }
            if (pst != null && !pst.isClosed()) {
                pst.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
