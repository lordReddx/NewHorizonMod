package newhorizon.expand.entities;

import arc.math.geom.QuadTree;
import arc.math.geom.Rect;
import arc.struct.ObjectMap;
import arc.struct.ObjectSet;
import arc.struct.OrderedSet;
import mindustry.Vars;
import mindustry.entities.EntityGroup;
import mindustry.game.Team;
import newhorizon.expand.block.special.CommandableBlock;
import newhorizon.expand.block.special.RemoteCoreStorage;
import newhorizon.util.feature.cutscene.CutsceneEventEntity;
import newhorizon.util.feature.cutscene.events.util.AutoEventTrigger;

public class NHGroups{
	public static final EntityGroup<CutsceneEventEntity> event = new EntityGroup<>(CutsceneEventEntity.class, false, true);
	public static final EntityGroup<AutoEventTrigger> autoEventTriggers = new EntityGroup<>(AutoEventTrigger.class, false, true);
	
	public static final ObjectMap<Integer, ObjectSet<RemoteCoreStorage.RemoteCoreStorageBuild>> placedRemoteCore = new ObjectMap<>(Team.all.length);
	public static final QuadTree<GravityTrapField> gravityTraps = new QuadTree<>(Vars.world.getQuadBounds(new Rect()));
	public static final OrderedSet<CommandableBlock.CommandableBlockBuild> commandableBuilds = new OrderedSet<>();
	
	public static void clear(){
		event.clear();
		gravityTraps.clear();
		RemoteCoreStorage.clear();
		commandableBuilds.clear();
	}
}