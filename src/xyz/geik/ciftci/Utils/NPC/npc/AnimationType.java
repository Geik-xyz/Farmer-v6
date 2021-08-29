package xyz.geik.ciftci.Utils.NPC.npc;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnimationType {
    SWING, TAKE_DAMAGE, LEAVE_BED, EAT_FOOD, CRITICAL_EFFECT, MAGIC_CRITICAL_EFFECT;
    
    public int getId(AnimationType type)
    {
    	
    	if (type.equals(AnimationType.SWING))
    		return 0;
    	
    	else if (type.equals(AnimationType.TAKE_DAMAGE))
    		return 1;
    	
    	else if (type.equals(AnimationType.LEAVE_BED))
    		return 2;
    	
    	else if (type.equals(AnimationType.EAT_FOOD))
    		return 3;
    	
    	else if (type.equals(AnimationType.CRITICAL_EFFECT))
    		return 4;
    	
    	else if (type.equals(AnimationType.MAGIC_CRITICAL_EFFECT))
    		return 5;
    	
    	else return 0;
    	
    }
}
