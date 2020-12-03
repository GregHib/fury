package com.fury.game.system.mysql.motivote;

import com.google.gson.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public final class MotivoteThread extends Thread
{
	private final Motivote<?> motivote;
	private boolean reward = false;
	private boolean running;

	public MotivoteThread(Motivote<?> motivote)
	{
		this.motivote = motivote;
		this.setName("Motivote");
		this.running = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		while(running) {
			try {

				/** FINALIZING VOTES **/
				if (!motivote.finalized.isEmpty()) {
					String ids = "";
					for (int i : motivote.finalized) {
						ids += i + ",";
					}
					ids = ids.substring(0, ids.length() - 1);

					HttpURLConnection httpcon = (HttpURLConnection) new URL(motivote.pageURL() + "?do=finalize&type=" + (reward ? "rewards" : "votes") + "&key=" + motivote.securityKey() + "&ids=" + ids).openConnection();
					httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
					BufferedReader r = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), Charset.forName("UTF-8")));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = r.readLine()) != null) {
						sb.append(line);
					}
					String out = sb.toString();
					r.close();

					if (out.equalsIgnoreCase("success")) {
						Motivote.print("Finalized " + motivote.finalized.size() + " " + (reward ? "rewards" : "votes"));
						motivote.finalized.clear();
					} else {
						Motivote.print(out);
					}
				}

				/** ADDING VOTES **/


				HttpURLConnection httpcon = (HttpURLConnection) new URL(motivote.pageURL() + "?do=pending&key=" + motivote.securityKey()).openConnection();
				httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
				BufferedReader r = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), Charset.forName("UTF-8")));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					sb.append(line);
				}
				String out = sb.toString();
				JsonParser parser = new JsonParser();
				JsonObject obj = (JsonObject) parser.parse(out);
				Gson builder = new GsonBuilder().create();

				if (obj.has("error")) {
					Motivote.print("Error: " + obj.get("error"));
				} else {
					reward = obj.get("reward").getAsBoolean();

					synchronized (motivote.pending) {
						synchronized (motivote.finalized) {
							JsonArray dataArray = null;

							if (reward && obj.has("rewards")) {
								dataArray = obj.get("rewards").getAsJsonArray();
							} else if (obj.has("votes")) {
								dataArray = obj.get("votes").getAsJsonArray();
							}

							if (dataArray != null) {

								for (JsonElement a : dataArray) {
									JsonObject v = (JsonObject) a;
									int internalID = Integer.parseInt(v.get("id").getAsString());

									if (!motivote.finalized.contains(internalID) && !motivote.pending.contains(internalID)) {
										motivote.pending.add(internalID);
										String user = v.get("user").getAsString();
										String ip = v.get("ip").getAsString();
										if (reward) {
											Reward re = new Reward(motivote, internalID, Integer.parseInt(v.get("incentive").getAsString()), user, ip, v.get("name").getAsString(), Integer.parseInt(v.get("amount").getAsString()));
											((Motivote<Reward>) motivote).handler().onCompletion(re);
										} else {
											Vote vo = new Vote(motivote, internalID, Integer.parseInt(v.get("site").getAsString()), user, ip);
											((Motivote<Vote>) motivote).handler().onCompletion(vo);
										}

										//motivote.pending.remove((Integer)internalID);
									}
								}
							}

							motivote.pending.clear();
						}
					}
				}


				/** 10 seconds sleep **/
				Thread.sleep(10000);
			} catch(Exception e) {
				//e.printStackTrace();
			}
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
