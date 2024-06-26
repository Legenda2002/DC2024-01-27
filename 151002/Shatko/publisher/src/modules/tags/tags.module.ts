import { Module } from "@nestjs/common";
import { TagsService } from "./tags.service";
import { TagsController } from "./tags.controller";
import { DrizzleProvider } from "../drizzle/drizzle.provider";

@Module({
  controllers: [TagsController],
  providers: [TagsService, DrizzleProvider],
})
export class TagsModule {}
