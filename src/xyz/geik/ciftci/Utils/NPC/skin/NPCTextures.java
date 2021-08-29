package xyz.geik.ciftci.Utils.NPC.skin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NPCTextures {
	
	public static String[] getSkin(String name)
	{
		
		try
		{
			
			if (name.equalsIgnoreCase("beerbellyman")) {
				
				String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYyMjE0Njk2MTAzNywKICAicHJvZmlsZUlkIiA6ICI0ZGI2MWRkOTM0Mzk0M2M0YjhhOTZiNDQwMWM3MDM1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiZWVyYmVsbHltYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MyZTgxOTkwNmViMTc5NDM5YjhkZDU1NTExMzJlNTRlYjQ3MTczZTBmNDU4ODYxYWQyYThjOTM3OTE4Mzg5MSIKICAgIH0KICB9Cn0=";
				
				String signature = "CMsZNwQbpxrUOjgmNUCFMLz8RaEBR68SlE0pUgWNw/MpT6F9ezcAdM60mkThuRaoBBjE1/SIjLWV39fArR/qawzjQGBvdn4TsCwW0Wn96Y+S9lBmhl9ApnvYaIpi7nrMZS8newxzPWpwce0QQHJNEYrgA2olVB9bosT4tW/aI6kzLC+raGjIR7F+srYnC36GKdroOeii8tUGMPsOC/7SrVxzAKVyUSkkfHz46PjBclQNShv4pJoHnxiaTaJC2oWAeTUEilMGpmVS0Sb28+mRyLydmU5RN4TvJtHZ0AVlZLPa3Q2X5FDT9e5/HhOJdegXiR9hfQQBe1+K/uFXfaBzqbxsKWubBG13g4uFwhcSaujCCgRXcEmTBo7/boUDHlaS4haJroPbLheINcLEX7cdvkd1aQuOI1Bhai6W2cP460MbYxxMg/Jrf8BAxEbY1qfu0aU97DJYPLGMQ5no0O5maWjNA99+bZJpukkzaI2n0J/zOi5ltjbVn03q3PZaF9ZbQfA2L+aPqPjL8te/K7NeV3gUXehWDWlokrsZOE0VQ++TeFFSGib5B/799BzKVvGWZQiDRAQb1KxWCVkzFBz9EuWExzodfrvEsQhedaCT402lpcCHJg0D7PLfFDpt97J5DwCjfTbylNqEDpkxFqoLbmmoVBiImn+TB4yQanzSIhE=";
				
				return new String[] { texture, signature };
				
			}
			
			else {
				
				URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
				
				InputStreamReader reader = new InputStreamReader(url.openStream());
				
				String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
				
				URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
				
				InputStreamReader reader2 = new InputStreamReader(url2.openStream());
				
				JsonObject property =  new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
				
				String texture = property.get("value").getAsString();
				
				String signature = property.get("signature").getAsString();
				
				return new String[] { texture, signature };
				
			}
			
		}
		
		catch (IOException | IllegalStateException e1)
		{
			
				String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYyMjE0Njk2MTAzNywKICAicHJvZmlsZUlkIiA6ICI0ZGI2MWRkOTM0Mzk0M2M0YjhhOTZiNDQwMWM3MDM1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiZWVyYmVsbHltYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MyZTgxOTkwNmViMTc5NDM5YjhkZDU1NTExMzJlNTRlYjQ3MTczZTBmNDU4ODYxYWQyYThjOTM3OTE4Mzg5MSIKICAgIH0KICB9Cn0=";
				
				String signature = "CMsZNwQbpxrUOjgmNUCFMLz8RaEBR68SlE0pUgWNw/MpT6F9ezcAdM60mkThuRaoBBjE1/SIjLWV39fArR/qawzjQGBvdn4TsCwW0Wn96Y+S9lBmhl9ApnvYaIpi7nrMZS8newxzPWpwce0QQHJNEYrgA2olVB9bosT4tW/aI6kzLC+raGjIR7F+srYnC36GKdroOeii8tUGMPsOC/7SrVxzAKVyUSkkfHz46PjBclQNShv4pJoHnxiaTaJC2oWAeTUEilMGpmVS0Sb28+mRyLydmU5RN4TvJtHZ0AVlZLPa3Q2X5FDT9e5/HhOJdegXiR9hfQQBe1+K/uFXfaBzqbxsKWubBG13g4uFwhcSaujCCgRXcEmTBo7/boUDHlaS4haJroPbLheINcLEX7cdvkd1aQuOI1Bhai6W2cP460MbYxxMg/Jrf8BAxEbY1qfu0aU97DJYPLGMQ5no0O5maWjNA99+bZJpukkzaI2n0J/zOi5ltjbVn03q3PZaF9ZbQfA2L+aPqPjL8te/K7NeV3gUXehWDWlokrsZOE0VQ++TeFFSGib5B/799BzKVvGWZQiDRAQb1KxWCVkzFBz9EuWExzodfrvEsQhedaCT402lpcCHJg0D7PLfFDpt97J5DwCjfTbylNqEDpkxFqoLbmmoVBiImn+TB4yQanzSIhE=";
				
				return new String[] { texture, signature };
			
		}
		
	}

    public static WrappedSignedProperty toProperty(String name) {
    	String[] skin = getSkin(name);
        return new WrappedSignedProperty("textures", skin[0], skin[1]);
    }
}
