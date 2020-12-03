package com.fileserver.tools;

public class DefDumper {
    /*public static void dumpObjectModels(int[] indices) {
        int dumped = 0, exceptions = 0;
        for(int i = 0; i < indices.length-1; i++) {
            ObjectDefinitions object = ObjectDefinitions.forId(i);
            if(object == null)
                continue;
            if(object.modelIds == null)
                continue;
            for(int model : object.modelIds[0]) {
                try {
                    byte abyte[] = clientInstance.indices[1].decompress(model);
                    File modelFile = new File(SignLink.getCacheDirectory().toString() + "/objectModels/" + model + ".gz");
                    FileOutputStream fos = new FileOutputStream(modelFile);
                    fos.write(abyte);
                    fos.close();
                    dumped++;
                } catch(Exception e) {
                    exceptions++;
                }
            }
        }
        System.out.println("Dumped "+dumped+" object models with "+exceptions+" exceptions.");
    }*/

    /*public static void dumpItemModelsForId(int i) {
        try {
            ItemDefinition d = get(i);

            if (d != null) {
                int[] models = {d.primaryMaleModel, d.primaryFemaleModel, d.modelId,};

                for (int ids : models) {// 13655
                    if (ids > 0) {
                        try {
                            System.out.println("Dumping item model: " + ids);
                            byte abyte[] = Client.instance.indices[1].decompress(ids);
                            File map = new File("./models/" + ids + ".gz");
                            FileOutputStream fos = new FileOutputStream(map);
                            fos.write(abyte);
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
