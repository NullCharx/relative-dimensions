package es.nullbyte.charmiscmods.items.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class tparticlePacket {
    private final double xinit;
    private final double yinit;
    private final double zinit;

    private final double xtarg;
    private final double ytarg;
    private final double ztarg;

    private int particleType;

    public tparticlePacket(double xinit, double yinit, double zinit, double xtarg, double ytarg, double ztarg, int particleType) {
        this.xinit = xinit;
        this.yinit = yinit;
        this.zinit = zinit;
        this.xtarg = xtarg;
        this.ytarg = ytarg;
        this.ztarg = ztarg;
        this.particleType = particleType;
    }

    public tparticlePacket(FriendlyByteBuf buf) {
        this.xinit = buf.readDouble();
        this.yinit = buf.readDouble();
        this.zinit = buf.readDouble();
        this.xtarg = buf.readDouble();
        this.ytarg = buf.readDouble();
        this.ztarg = buf.readDouble();
        this.particleType = buf.readInt();
    }

    public  void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(xinit);
        buf.writeDouble(yinit);
        buf.writeDouble(zinit);
        buf.writeDouble(xtarg);
        buf.writeDouble(ytarg);
        buf.writeDouble(ztarg);
        buf.writeInt(particleType);
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        //Get the reciever of the packet (player)
        if (ctx.isServerSide()) {
            if (ctx.isClientSide()) {
                Player player = Minecraft.getInstance().player;
                Level playerLevel = player.level();
                if (player != null) {
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 1, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 2, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 3, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 4, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 5, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 6, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 7, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 8, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 9, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 10, zinit, 0.0D, 0.0D, 0.0D);

                    //and at target
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 1, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 2, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 3, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 4, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 5, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 6, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 7, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 8, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 9, zinit, 0.0D, 0.0D, 0.0D);
                    playerLevel.addParticle(ParticleTypes.PORTAL, xinit, yinit + 10, zinit, 0.0D, 0.0D, 0.0D);

                    // Additional server-side logic if needed
                }
            }
        }
        ctx.setPacketHandled(true);
    }
    public double getXinit() {
        return xinit;
    }

    public double getYinit() {
        return yinit;
    }

    public double getZinit() {
        return zinit;
    }

    public double getXtarg() {
        return xtarg;
    }

    public double getYtarg() {
        return ytarg;
    }

    public double getZtarg() {
        return ztarg;
    }


    public int getParticleType() {
        return particleType;
    }
}
