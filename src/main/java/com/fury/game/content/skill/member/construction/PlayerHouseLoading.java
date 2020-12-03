package com.fury.game.content.skill.member.construction;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.object.GameObject;
import com.fury.game.system.files.Resources;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Chris on 2/10/2017.
 */
public class PlayerHouseLoading {

    public static House loadPlayerHouse(Player player) throws IOException {
        String playerName = player.getUsername();
        File houseFile = new File(Resources.getSaveDirectory("construction") + playerName + ".house");
        boolean doesSaveExist = houseFile.exists();
        if(!doesSaveExist) {
            houseFile.createNewFile();
            return null;
        }

        House d = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(houseFile));
            d = (House) in.readObject();
            in.close();
            //System.out.println("umm:" +d.objects.size());
        } catch (Exception e) {
            //	e.printStackTrace();
            return null;
        }
        return d;

    }

    public ArrayList deserialzeRooms(File file) throws IOException, ClassNotFoundException{
        ArrayList arraylist=new ArrayList();
        ObjectInputStream ois = null;
        FileInputStream fin = null;
        try{
            fin = new FileInputStream(file);
            if(fin.available()!=0)
            {
                while (true) {
                    ois = new ObjectInputStream(fin);
                    arraylist.add((House.RoomReference)ois.readObject());
                }}}catch (EOFException e) {
        // as expected
        } finally {
            if (fin != null)
                ois.close();
            System.out.println(arraylist.size());
            return arraylist;
        }
    }

    public ArrayList deserialzeObjects(File file) throws IOException, ClassNotFoundException{
        ArrayList arraylist=new ArrayList();
        ObjectInputStream ois = null;
        FileInputStream fin = null;
        try{
            fin = new FileInputStream(file);
            if(fin.available()!=0)
            {
                while (true) {
                    ois = new ObjectInputStream(fin);
                    arraylist.add((GameObject)ois.readObject());
                }}}catch (EOFException e) {
            // as expected
        } finally {
            if (fin != null)
                ois.close();
            System.out.println(arraylist.size());
            return arraylist;
        }
    }

}
