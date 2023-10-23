package surreal.bundles.mixins.interfaces;

import surreal.bundles.mixins.early.CPacketClickWindowMixin;

public interface IPacketClickCtrlHeld {
    
    public boolean getCtrlHeld();
    public CPacketClickWindowMixin setCtrlHeld(boolean value);
}
