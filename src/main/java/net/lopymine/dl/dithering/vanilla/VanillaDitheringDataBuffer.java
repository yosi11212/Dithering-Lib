package net.lopymine.dl.dithering.vanilla;

import com.mojang.blaze3d.buffers.*;
//? if >=26.3 {
/*import com.mojang.renderpearl.api.buffers.*;
import com.mojang.renderpearl.frontend.FrontendCommandEncoder;
import java.util.*;
import net.lopymine.dl.client.DitheringLibClient;
import org.jetbrains.annotations.Nullable;
*///?}
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import net.lopymine.dl.dithering.DitheringData;
import org.lwjgl.system.MemoryStack;

//? if >=26.3 {
/*// Since 26.3 buffers can't be written while a render pass is open, so every DitheringData used in a frame
// gets its own slot written before the render passes, and inside a render pass update() only selects that slot
public class VanillaDitheringDataBuffer {

	private static final int SIZE = new Std140SizeCalculator()
			.putFloat()
			.putFloat()
			.putFloat()
			.putFloat()
			.putFloat()
			.get();

	private static final Map<Values, Integer> SLOTS = new HashMap<>();
	private static final List<Values> ENTRIES = new ArrayList<>();
	private static final List<GpuBuffer> RETIRED_BUFFERS = new ArrayList<>();

	@Nullable
	private static GpuBuffer buffer;
	private static int capacity;
	private static int stride;
	@Nullable
	private static GpuBufferSlice current;
	private static boolean warned;

	public static void startFrame() {
		RETIRED_BUFFERS.forEach(GpuBuffer::close);
		RETIRED_BUFFERS.clear();
		SLOTS.clear();
		ENTRIES.clear();
		current = null;
		update(new DitheringData());
	}

	public static void stage(DitheringData data) {
		if (isInRenderPass()) {
			return;
		}
		Values values = Values.of(data);
		if (SLOTS.containsKey(values)) {
			return;
		}
		SLOTS.put(values, ENTRIES.size());
		ENTRIES.add(values);
		if (buffer == null || ENTRIES.size() > capacity) {
			if (buffer != null) {
				RETIRED_BUFFERS.add(buffer);
			}
			int alignment = RenderSystem.getDevice().getDeviceInfo().limits().minUniformOffsetAlignment();
			stride   = (SIZE + alignment - 1) / alignment * alignment;
			capacity = Math.max(16, capacity * 2);
			buffer   = RenderSystem.getDevice().createBuffer(() -> "VanillaDitheringDataBuffer UBO", 136, (long) capacity * stride);
			for (int i = 0; i < ENTRIES.size(); i++) {
				write(i, ENTRIES.get(i));
			}
			return;
		}
		write(ENTRIES.size() - 1, values);
	}

	public static void update(DitheringData data) {
		stage(data);
		Integer slot = SLOTS.get(Values.of(data));
		if (slot == null || buffer == null) {
			if (!warned) {
				warned = true;
				DitheringLibClient.LOGGER.warn("DitheringData wasn't staged before the render pass, previous data will be used", new Throwable());
			}
			return;
		}
		current = buffer.slice((long) slot * stride, SIZE);
	}

	@Nullable
	public static GpuBufferSlice getSlice() {
		return current;
	}

	@Nullable
	public static GpuBuffer getBuffer() {
		return buffer;
	}

	private static void write(int slot, Values values) {
		try (MemoryStack memoryStack = MemoryStack.stackPush()) {
			ByteBuffer byteBuffer = Std140Builder.onStack(memoryStack, SIZE)
					.putFloat(values.far())
					.putFloat(values.near())
					.putFloat(values.minValue())
					.putFloat(values.fixedValue())
					.putFloat(values.pixelSize())
					.get();
			RenderSystem.getDevice().createCommandEncoder().writeToBuffer(buffer.slice((long) slot * stride, SIZE), byteBuffer);
		}
	}

	private static boolean isInRenderPass() {
		return RenderSystem.getDevice().createCommandEncoder() instanceof FrontendCommandEncoder encoder && encoder.isInRenderPass();
	}

	private record Values(float far, float near, float minValue, float fixedValue, float pixelSize) {

		private static Values of(DitheringData data) {
			return new Values(
					data.getFar(),
					data.getNear(),
					(float) Math.clamp(data.getMinValue(), 0.0, 1.0F),
					(float) Math.clamp(data.getFixedValue(), 0.0, 1.0F),
					data.getPixelSize()
			);
		}
	}
}
*///?} else {
public class VanillaDitheringDataBuffer {

	private static final int SIZE = new Std140SizeCalculator()
			.putFloat()
			.putFloat()
			.putFloat()
			.putFloat()
			.putFloat()
			.get();

	private static final GpuBuffer BUFFER = RenderSystem.getDevice().createBuffer(() -> "VanillaDitheringDataBuffer UBO", 136, SIZE);

	public static void update(DitheringData data) {
		try (MemoryStack memoryStack = MemoryStack.stackPush()) {
			ByteBuffer byteBuffer = Std140Builder.onStack(memoryStack, SIZE)
					.putFloat(data.getFar())
					.putFloat(data.getNear())
					.putFloat((float) Math.clamp(data.getMinValue(), 0.0, 1.0F))
					.putFloat((float) Math.clamp(data.getFixedValue(), 0.0, 1.0F))
					.putFloat(data.getPixelSize())
					.get();
			RenderSystem.getDevice().createCommandEncoder().writeToBuffer(BUFFER.slice(), byteBuffer);
		}
	}

	public static GpuBuffer getBuffer() {
		return BUFFER;
	}
}
//?}
