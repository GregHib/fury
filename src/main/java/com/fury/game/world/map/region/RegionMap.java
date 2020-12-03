package com.fury.game.world.map.region;

import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.clip.Flags;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Greg on 23/12/2016.
 */
public class RegionMap {

    private int regionX;
    private int regionY;
    private boolean clippedOnly;
    protected Map<Integer, Integer> masks;
    Map<Integer, OldRegionTile> tiles;

    private int toHash(int x, int y, int z) {
        return y + (x << 14) + (z << 28);
    }

    public OldRegionTile getTile(int x, int y, int z) {
        if(tiles == null)
             tiles = new HashMap<>();
        int hash = toHash(x, y, z);
        if(!tiles.containsKey(hash))
            tiles.put(hash, new OldRegionTile());
        return tiles.get(hash);
    }

    public boolean tileExists(int x, int y, int z) {
        if (tiles == null)
            return false;
        int hash = toHash(x, y, z);
        return tiles.containsKey(hash);
    }

    public RegionMap(int regionId, boolean clippedOnly) {
        regionX = (regionId >> 8) * 64;
        regionY = (regionId & 0xff) * 64;
        this.clippedOnly = clippedOnly;
        masks = new HashMap<>();
    }

    public int getMask(int x, int y, int z) {
        int hash = toHash(x, y, z);
        if(masks != null && masks.containsKey(hash))
            return masks.get(hash);
        return 0;
    }

    private void setMasks(int x, int y, int z, int mask) {
        int hash = toHash(x, y, z);
        if(mask == 0 && masks.containsKey(hash))
            masks.remove(hash);
        if(mask == 0)
            return;
        masks.put(hash, mask);
    }

    public int getRegionX() {
        return regionX;
    }

    public int getRegionY() {
        return regionY;
    }

    public void addUnwalkable(int x, int y, int z) {
        addMask(x, y, z, Flags.FLOOR_BLOCKSWALK);
    }

    public void addFloor(int x, int y, int z) {
        addMask(x, y, z, Flags.FLOORDECO_BLOCKSWALK);
    }

    public void removeFloor(int x, int y, int z) {
        removeMask(x, y, z, Flags.FLOORDECO_BLOCKSWALK);
    }

    public void addObject(int x, int y, int z, int sizeX, int sizeY, boolean solid, boolean notAlternative) {
        int mask = Flags.OBJ;
        if (solid)
            mask |= Flags.OBJ_BLOCKSFLY;
        if(notAlternative)
            mask |= Flags.OBJ_BLOCKSWALK_ALTERNATIVE;
        for (int i = x; i < x + sizeX; i++)
            for (int i2 = y; i2 < y + sizeY; i2++)
                addMask(i, i2, z, mask);
    }

    public void removeObject(int x, int y, int z, int sizeX, int sizeY, boolean solid, boolean notAlternative) {
        int mask = Flags.OBJ;
        if (solid)
            mask |= Flags.OBJ_BLOCKSFLY;
        if(notAlternative)
            mask |= Flags.OBJ_BLOCKSWALK_ALTERNATIVE;
        for (int i = x; i < x + sizeX; i++)
            for (int i2 = y; i2 < y + sizeY; i2++)
                removeMask(i, i2, z, mask);
    }

    public void addWall(int x, int y, int z, int type, int direction, boolean solid, boolean notAlternative) {
        if (type == 0) {
            if (direction == 0) {
                addMask(x, y, z, 128);
                addMask(x - 1, y, z, 8);
            } else if (direction == 1) {
                addMask(x, y, z, 2);
                addMask(x, y + 1, z, 32);
            } else if (direction == 2) {
                addMask(x, y, z, 8);
                addMask(x + 1, y, z, 128);
            } else if (direction == 3) {
                addMask(x, y, z, 32);
                addMask(x, y - 1, z, 2);
            }
        } else if (type == 1 || type == 3) {
            if (direction == 0) {
                addMask(x, y, z, 1);
                addMask(x - 1, y, z, 16);
            } else if (direction == 1) {
                addMask(x, y, z, 4);
                addMask(x + 1, y + 1, z, 64);
            } else if (direction == 2) {
                addMask(x, y, z, 16);
                addMask(x + 1, y - 1, z, 1);
            } else if (direction == 3) {
                addMask(x, y, z, 64);
                addMask(x - 1, y - 1, z, 4);
            }
        } else if (type == 2) {
            if (direction == 0) {
                addMask(x, y, z, 130);
                addMask(x - 1, y, z, 8);
                addMask(x, y + 1, z, 32);
            } else if (direction == 1) {
                addMask(x, y, z, 10);
                addMask(x, y + 1, z, 32);
                addMask(x + 1, y, z, 128);
            } else if (direction == 2) {
                addMask(x, y, z, 40);
                addMask(x + 1, y, z, 128);
                addMask(x, y - 1, z, 2);
            } else if (direction == 3) {
                addMask(x, y, z, 160);
                addMask(x, y - 1, z, 2);
                addMask(x - 1, y, z, 8);
            }
        }
        if (solid) {
            if (type == 0) {
                if (direction == 0) {
                    addMask(x, y, z, 65536);
                    addMask(x - 1, y, z, 4096);
                } else if (direction == 1) {
                    addMask(x, y, z, 1024);
                    addMask(x, y + 1, z, 16384);
                } else if (direction == 2) {
                    addMask(x, y, z, 4096);
                    addMask(x + 1, y, z, 65536);
                } else if (direction == 3) {
                    addMask(x, y, z, 16384);
                    addMask(x, y - 1, z, 1024);
                }
            }
            if (type == 1 || type == 3) {
                if (direction == 0) {
                    addMask(x, y, z, 512);
                    addMask(x - 1, y + 1, z, 8192);
                } else if (direction == 1) {
                    addMask(x, y, z, 2048);
                    addMask(x + 1, y + 1, z, 32768);
                } else if (direction == 2) {
                    addMask(x, y, z, 8192);
                    addMask(x + 1, y + 1, z, 512);
                } else if (direction == 3) {
                    addMask(x, y, z, 32768);
                    addMask(x - 1, y - 1, z, 2048);
                }
            } else if (type == 2) {
                if (direction == 0) {
                    addMask(x, y, z, 66560);
                    addMask(x - 1, y, z, 4096);
                    addMask(x, y + 1, z, 16384);
                } else if (direction == 1) {
                    addMask(x, y, z, 5120);
                    addMask(x, y + 1, z, 16384);
                    addMask(x + 1, y, z, 65536);
                } else if (direction == 2) {
                    addMask(x, y, z, 20480);
                    addMask(x + 1, y, z, 65536);
                    addMask(x, y - 1, z, 1024);
                } else if (direction == 3) {
                    addMask(x, y, z, 81920);
                    addMask(x, y - 1, z, 1024);
                    addMask(x - 1, y, z, 4096);
                }
            }
        }
        if (notAlternative) {
            if (type == 0) {
                if (direction == 0) {
                    addMask(x, y, z, 0x20000000);
                    addMask(x - 1, y, z, 0x2000000);
                }
                if (direction == 1) {
                    addMask(x, y, z, 0x800000);
                    addMask(x, y + 1, z, 0x8000000);
                }
                if (direction == 2) {
                    addMask(x, y, z, 0x2000000);
                    addMask(x + 1, y, z, 0x20000000);
                }
                if (direction == 3) {
                    addMask(x, y, z, 0x8000000);
                    addMask(x, y - 1, z, 0x800000);
                }
            }
            if (type == 1 || type == 3) {
                if (direction == 0) {
                    addMask(x, y, z, 0x400000);
                    addMask(x - 1, y + 1, z, 0x4000000);
                }
                if (direction == 1) {
                    addMask(x, y, z, 0x1000000);
                    addMask(1 + x, 1 + y, z, 0x10000000);
                }
                if (direction == 2) {
                    addMask(x, y, z, 0x4000000);
                    addMask(x + 1, -1 + y, z, 0x400000);
                }
                if (direction == 3) {
                    addMask(x, y, z, 0x10000000);
                    addMask(-1 + x, y - 1, z, 0x1000000);
                }
            }
            if (type == 2) {
                if (direction == 0) {
                    addMask(x, y, z, 0x20800000);
                    addMask(-1 + x, y, z, 0x2000000);
                    addMask(x, 1 + y, z, 0x8000000);
                }
                if (direction == 1) {
                    addMask(x, y, z, 0x2800000);
                    addMask(x, 1 + y, z, 0x8000000);
                    addMask(x + 1, y, z, 0x20000000);
                }
                if (direction == 2) {
                    addMask(x, y, z, 0xa000000);
                    addMask(1 + x, y, z, 0x20000000);
                    addMask(x, y - 1, z, 0x800000);
                }
                if (direction == 3) {
                    addMask(x, y, z, 0x28000000);
                    addMask(x, y - 1, z, 0x800000);
                    addMask(-1 + x, y, z, 0x2000000);
                }
            }
        }
    }

    public void removeWall(int x, int y, int z, int type, int direction, boolean solid, boolean notAlternative) {
        if (type == 0) {
            if (direction == 0) {
                removeMask(x, y, z, 128);
                removeMask(x - 1, y, z, 8);
            } else if (direction == 1) {
                removeMask(x, y, z, 2);
                removeMask(x, y + 1, z, 32);
            } else if (direction == 2) {
                removeMask(x, y, z, 8);
                removeMask(x + 1, y, z, 128);
            } else if (direction == 3) {
                removeMask(x, y, z, 32);
                removeMask(x, y - 1, z, 2);
            }
        } else if (type == 1 || type == 3) {
            if (direction == 0) {
                removeMask(x, y, z, 1);
                removeMask(x - 1, y, z, 16);
            } else if (direction == 1) {
                removeMask(x, y, z, 4);
                removeMask(x + 1, y + 1, z, 64);
            } else if (direction == 2) {
                removeMask(x, y, z, 16);
                removeMask(x + 1, y - 1, z, 1);
            } else if (direction == 3) {
                removeMask(x, y, z, 64);
                removeMask(x - 1, y - 1, z, 4);
            }
        } else if (type == 2) {
            if (direction == 0) {
                removeMask(x, y, z, 130);
                removeMask(x - 1, y, z, 8);
                removeMask(x, y + 1, z, 32);
            } else if (direction == 1) {
                removeMask(x, y, z, 10);
                removeMask(x, y + 1, z, 32);
                removeMask(x + 1, y, z, 128);
            } else if (direction == 2) {
                removeMask(x, y, z, 40);
                removeMask(x + 1, y, z, 128);
                removeMask(x, y - 1, z, 2);
            } else if (direction == 3) {
                removeMask(x, y, z, 160);
                removeMask(x, y - 1, z, 2);
                removeMask(x - 1, y, z, 8);
            }
        }
        if (solid) {
            if (type == 0) {
                if (direction == 0) {
                    removeMask(x, y, z, 65536);
                    removeMask(x - 1, y, z, 4096);
                } else if (direction == 1) {
                    removeMask(x, y, z, 1024);
                    removeMask(x, y + 1, z, 16384);
                } else if (direction == 2) {
                    removeMask(x, y, z, 4096);
                    removeMask(x + 1, y, z, 65536);
                } else if (direction == 3) {
                    removeMask(x, y, z, 16384);
                    removeMask(x, y - 1, z, 1024);
                }
            }
            if (type == 1 || type == 3) {
                if (direction == 0) {
                    removeMask(x, y, z, 512);
                    removeMask(x - 1, y + 1, z, 8192);
                } else if (direction == 1) {
                    removeMask(x, y, z, 2048);
                    removeMask(x + 1, y + 1, z, 32768);
                } else if (direction == 2) {
                    removeMask(x, y, z, 8192);
                    removeMask(x + 1, y + 1, z, 512);
                } else if (direction == 3) {
                    removeMask(x, y, z, 32768);
                    removeMask(x - 1, y - 1, z, 2048);
                }
            } else if (type == 2) {
                if (direction == 0) {
                    removeMask(x, y, z, 66560);
                    removeMask(x - 1, y, z, 4096);
                    removeMask(x, y + 1, z, 16384);
                } else if (direction == 1) {
                    removeMask(x, y, z, 5120);
                    removeMask(x, y + 1, z, 16384);
                    removeMask(x + 1, y, z, 65536);
                } else if (direction == 2) {
                    removeMask(x, y, z, 20480);
                    removeMask(x + 1, y, z, 65536);
                    removeMask(x, y - 1, z, 1024);
                } else if (direction == 3) {
                    removeMask(x, y, z, 81920);
                    removeMask(x, y - 1, z, 1024);
                    removeMask(x - 1, y, z, 4096);
                }
            }
        }
        if (notAlternative) {
            if (type == 0) {
                if (direction == 0) {
                    removeMask(x, y, z, 0x20000000);
                    removeMask(x - 1, y, z, 0x2000000);
                }
                if (direction == 1) {
                    removeMask(x, y, z, 0x800000);
                    removeMask(x, y + 1, z, 0x8000000);
                }
                if (direction == 2) {
                    removeMask(x, y, z, 0x2000000);
                    removeMask(x + 1, y, z, 0x20000000);
                }
                if (direction == 3) {
                    removeMask(x, y, z, 0x8000000);
                    removeMask(x, y - 1, z, 0x800000);
                }
            }
            if (type == 1 || type == 3) {
                if (direction == 0) {
                    removeMask(x, y, z, 0x400000);
                    removeMask(x - 1, y + 1, z, 0x4000000);
                }
                if (direction == 1) {
                    removeMask(x, y, z, 0x1000000);
                    removeMask(1 + x, 1 + y, z, 0x10000000);
                }
                if (direction == 2) {
                    removeMask(x, y, z, 0x4000000);
                    removeMask(x + 1, -1 + y, z, 0x400000);
                }
                if (direction == 3) {
                    removeMask(x, y, z, 0x10000000);
                    removeMask(-1 + x, y - 1, z, 0x1000000);
                }
            }
            if (type == 2) {
                if (direction == 0) {
                    removeMask(x, y, z, 0x20800000);
                    removeMask(-1 + x, y, z, 0x2000000);
                    removeMask(x, 1 + y, z, 0x8000000);
                }
                if (direction == 1) {
                    removeMask(x, y, z, 0x2800000);
                    removeMask(x, 1 + y, z, 0x8000000);
                    removeMask(x + 1, y, z, 0x20000000);
                }
                if (direction == 2) {
                    removeMask(x, y, z, 0xa000000);
                    removeMask(1 + x, y, z, 0x20000000);
                    removeMask(x, y - 1, z, 0x800000);
                }
                if (direction == 3) {
                    removeMask(x, y, z, 0x28000000);
                    removeMask(x, y - 1, z, 0x800000);
                    removeMask(-1 + x, y, z, 0x2000000);
                }
            }
        }
    }

    public void setMask(int x, int y, int z, int mask) {
        if (x >= 64 || y >= 64 || x < 0 || y < 0) {
            Position tile = new Position(regionX + x, regionY + y, z);
            int regionId = tile.getRegionId();
            int newRegionX = (regionId >> 8) * 64;
            int newRegionY = (regionId & 0xff) * 64;
            if (clippedOnly)
                GameWorld.getRegions().get(regionId).forceGetRegionMapClippedOnly().setMask(tile.getX() - newRegionX, tile.getY() - newRegionY, z, mask);
            else
                GameWorld.getRegions().get(regionId).forceGetRegionMap().setMask(tile.getX() - newRegionX, tile.getY() - newRegionY, z, mask);
            return;
        }
        setMasks(x, y, z, mask);
    }

    public void addMask(int x, int y, int z, int mask) {
        if (x >= 64 || y >= 64 || x < 0 || y < 0) {
            Position tile = new Position(regionX + x, regionY + y, z);
            int regionId = tile.getRegionId();
            int newRegionX = (regionId >> 8) * 64;
            int newRegionY = (regionId & 0xff) * 64;
            if (clippedOnly)
                GameWorld.getRegions().get(regionId).forceGetRegionMapClippedOnly().addMask(tile.getX() - newRegionX, tile.getY() - newRegionY, z, mask);
            else
                GameWorld.getRegions().get(regionId).forceGetRegionMap().addMask(tile.getX() - newRegionX, tile.getY() - newRegionY, z, mask);
            return;
        }
        setMasks(x, y, z, getMask(x, y, z) | mask);
    }

    public void removeMask(int x, int y, int z, int mask) {
        if(x >= 64 || y >= 64 || x < 0 || y < 0) {
            Position tile = new Position(regionX + x, regionY + y, z);
            int regionId = tile.getRegionId();
            int newRegionX = (regionId >> 8) * 64;
            int newRegionY = (regionId & 0xff) * 64;
            if (clippedOnly)
                GameWorld.getRegions().get(tile.getRegionId()).forceGetRegionMapClippedOnly().removeMask(tile.getX() - newRegionX, tile.getY() - newRegionY, z, mask);
            else
                GameWorld.getRegions().get(tile.getRegionId()).forceGetRegionMap().removeMask(tile.getX() - newRegionX, tile.getY() - newRegionY, z, mask);
        }
        setMasks(x, y, z, getMask(x, y, z) & (~mask));
    }
}