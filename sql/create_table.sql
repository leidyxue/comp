CREATE TYPE chart_meta_type AS ENUM ('DRILL', 'Y_OPT', 'Y', 'X');
CREATE TYPE chart_type AS ENUM ('TABLE', 'BASIC_BAR', 'BASIC_LINE', 'BIAXIAL', 'CARD', 'BASIC_PIE');
CREATE TYPE dash_element_type AS ENUM ('CHART', 'TEXT');
CREATE TYPE ds_type AS ENUM ('MYSQL', 'POSTGRESQL', 'KYLIN', 'CSV');
CREATE TYPE field_gen_type AS ENUM ('NATIVE', 'T_GENERATE', 'C_GENERATE');
CREATE TYPE field_sort_type AS ENUM ('DEFAULT', 'ASC', 'DESC');
CREATE TYPE field_value_type AS ENUM ('NUM', 'TEXT', 'DATE');
CREATE TYPE table_type AS ENUM ('CSV', 'EXCEL', 'GENERATE', 'NATIVE', 'PUBLIC', 'SHARE');
CREATE TYPE bi_ele_type AS ENUM ('TABLE', 'FIELD', 'DATASOURCE');

CREATE TABLE "public"."dir" (
"id" text COLLATE "default" NOT NULL,
"name" text COLLATE "default" NOT NULL,
"parent_id" text COLLATE "default",
"create_time" timestamp(6) NOT NULL,
"modify_time" timestamp(6) NOT NULL,
"owner" text COLLATE "default" NOT NULL,
CONSTRAINT "dir_pkey" PRIMARY KEY ("id"),
CONSTRAINT "dir_parent_id_fkey" FOREIGN KEY ("parent_id") REFERENCES "public"."dir" ("id") ON DELETE RESTRICT ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);
CREATE UNIQUE INDEX "dir_name_idx" ON "public"."dir" USING btree ("name", "owner");

CREATE TABLE "public"."datasource" (
"id" text COLLATE "default" NOT NULL,
"name" text COLLATE "default" NOT NULL,
"type" "public"."ds_type" NOT NULL,
"parameter" jsonb NOT NULL,
"create_time" timestamp(6),
"modify_time" timestamp(6),
"owner" text COLLATE "default",
"desc" text COLLATE "default",
CONSTRAINT "datasource_pkey" PRIMARY KEY ("id")
)
WITH (OIDS=FALSE);
COMMENT ON TABLE "public"."datasource" IS '数据源表';
COMMENT ON COLUMN "public"."datasource"."id" IS '数据源ID';
COMMENT ON COLUMN "public"."datasource"."name" IS '数据源名称';
COMMENT ON COLUMN "public"."datasource"."type" IS '数据源类型';
COMMENT ON COLUMN "public"."datasource"."parameter" IS '数据源参数';
COMMENT ON COLUMN "public"."datasource"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."datasource"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."datasource"."owner" IS '所属用户';
CREATE UNIQUE INDEX "datasource_name_owner_idx" ON "public"."datasource" USING btree ("name", "owner");

CREATE TABLE "public"."table" (
"id" text COLLATE "default" NOT NULL,
"name" text COLLATE "default" NOT NULL,
"origin_name" text COLLATE "default",
"desc" text COLLATE "default",
"type" "public"."table_type" NOT NULL,
"parameter" jsonb,
"create_time" timestamp(6) NOT NULL,
"modify_time" timestamp(6) NOT NULL,
"owner" text COLLATE "default" NOT NULL,
"parent_id" text COLLATE "default",
"ds_id" text COLLATE "default" NOT NULL,
CONSTRAINT "table_pkey" PRIMARY KEY ("id"),
CONSTRAINT "table_ds_id_fkey" FOREIGN KEY ("ds_id") REFERENCES "public"."datasource" ("id") ON DELETE CASCADE ON UPDATE NO ACTION,
CONSTRAINT "table_parent_id_fkey" FOREIGN KEY ("parent_id") REFERENCES "public"."dir" ("id") ON DELETE RESTRICT ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;
COMMENT ON TABLE "public"."table" IS '工作表';
COMMENT ON COLUMN "public"."table"."id" IS '工作表ID';
COMMENT ON COLUMN "public"."table"."name" IS '工作表原始名称';
COMMENT ON COLUMN "public"."table"."desc" IS '工作表描述';
COMMENT ON COLUMN "public"."table"."type" IS '工作表类型';
COMMENT ON COLUMN "public"."table"."parameter" IS '工作表配置，一般用于生成工作表';
COMMENT ON COLUMN "public"."table"."create_time" IS '工作表创建时间';
COMMENT ON COLUMN "public"."table"."modify_time" IS '工作表修改时间';
COMMENT ON COLUMN "public"."table"."owner" IS '工作表所有者';
COMMENT ON COLUMN "public"."table"."ds_id" IS '工作表来源数据源';
CREATE UNIQUE INDEX "table_ds_id_name_idx" ON "public"."table" USING btree ("ds_id", "name", "owner");

CREATE TABLE "public"."field" (
"id" text COLLATE "default" NOT NULL,
"name" text COLLATE "default" NOT NULL,
"origin_name" text COLLATE "default",
"type" "public"."field_value_type" NOT NULL,
"gen_type" "public"."field_gen_type" NOT NULL,
"aggregator" text COLLATE "default",
"parameter" text COLLATE "default",
"owner" text COLLATE "default" NOT NULL,
"table_id" text COLLATE "default" NOT NULL,
"create_time" timestamp(6) NOT NULL,
"modify_time" timestamp(6) NOT NULL,
"desc" text COLLATE "default",
"origin_type" text COLLATE "default",
"f_origin_name" text COLLATE "default",
"sort_id" int4,
CONSTRAINT "field_pkey" PRIMARY KEY ("id"),
CONSTRAINT "table_id" FOREIGN KEY ("table_id") REFERENCES "public"."table" ("id") ON DELETE CASCADE ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);
COMMENT ON TABLE "public"."field" IS '字段表';
COMMENT ON COLUMN "public"."field"."id" IS '字段id';
COMMENT ON COLUMN "public"."field"."name" IS '字段别名';
COMMENT ON COLUMN "public"."field"."origin_name" IS '字段原始名称';
COMMENT ON COLUMN "public"."field"."type" IS '字段值类型';
COMMENT ON COLUMN "public"."field"."gen_type" IS '字段类型';
COMMENT ON COLUMN "public"."field"."aggregator" IS '字段表达式，如果是生成字段此项有值';
COMMENT ON COLUMN "public"."field"."owner" IS '字段所有者';
COMMENT ON COLUMN "public"."field"."table_id" IS '字段所属表';
COMMENT ON COLUMN "public"."field"."create_time" IS '字段创建时间';
COMMENT ON COLUMN "public"."field"."modify_time" IS '字段修改时间';
COMMENT ON COLUMN "public"."field"."f_origin_name" IS '文件中的字段名称';
CREATE UNIQUE INDEX "field_table_id_name_idx" ON "public"."field" USING btree ("table_id", "name");
CREATE UNIQUE INDEX "field_table_id_origin_name_idx" ON "public"."field" USING btree ("table_id", "origin_name");

CREATE TABLE "public"."project" (
"id" text COLLATE "default" NOT NULL,
"name" text COLLATE "default" NOT NULL,
"parent_id" text COLLATE "default",
"create_time" timestamp(6) NOT NULL,
"modify_time" timestamp(6) NOT NULL,
"owner" text COLLATE "default" NOT NULL,
CONSTRAINT "project_pkey" PRIMARY KEY ("id"),
CONSTRAINT "project_parent_id_fkey" FOREIGN KEY ("parent_id") REFERENCES "public"."project" ("id") ON DELETE RESTRICT ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);
COMMENT ON TABLE "public"."project" IS '项目表';
COMMENT ON COLUMN "public"."project"."id" IS '目录id';
COMMENT ON COLUMN "public"."project"."name" IS '目录名称';
COMMENT ON COLUMN "public"."project"."parent_id" IS '目录父节点的id,如果在根节点下则为空';
COMMENT ON COLUMN "public"."project"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."project"."modify_time" IS '修改时间';
CREATE UNIQUE INDEX "project_name_owner_idx" ON "public"."project" USING btree ("name", "owner");

CREATE TABLE "public"."dashboard" (
"id" text COLLATE "default" NOT NULL,
"name" text COLLATE "default" NOT NULL,
"desc" text COLLATE "default",
"owner" text COLLATE "default" NOT NULL,
"project_id" text COLLATE "default",
"create_time" timestamp(6) NOT NULL,
"modify_time" timestamp(6) NOT NULL,
"web_data" jsonb,
CONSTRAINT "dashboard_pkey" PRIMARY KEY ("id"),
CONSTRAINT "dashboard_project_id_fkey" FOREIGN KEY ("project_id") REFERENCES "public"."project" ("id") ON DELETE CASCADE ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);
COMMENT ON TABLE "public"."dashboard" IS '仪表盘表';
COMMENT ON COLUMN "public"."dashboard"."id" IS '仪表盘id';
COMMENT ON COLUMN "public"."dashboard"."name" IS '仪表盘名称';
COMMENT ON COLUMN "public"."dashboard"."desc" IS '仪表盘描述';
COMMENT ON COLUMN "public"."dashboard"."owner" IS '仪表盘所有者';
COMMENT ON COLUMN "public"."dashboard"."project_id" IS '仪表盘所在的项目的id，如果仪表盘在根项目则该字段为空';
COMMENT ON COLUMN "public"."dashboard"."create_time" IS '仪表盘创建时间';
COMMENT ON COLUMN "public"."dashboard"."modify_time" IS '仪表盘修改时间';
CREATE UNIQUE INDEX "dashboard_name_owner_idx" ON "public"."dashboard" USING btree ("name", "owner");

CREATE TABLE "public"."chart" (
"id" text COLLATE "default" NOT NULL,
"name" text COLLATE "default" NOT NULL,
"table_id" text COLLATE "default" NOT NULL,
"owner" text COLLATE "default" NOT NULL,
"create_time" timestamp(6),
"modify_time" timestamp(6),
"dash_id" text COLLATE "default" NOT NULL,
"web_data" jsonb NOT NULL,
"chart_type" "public"."chart_type",
"desc" text COLLATE "default",
"filter_exp" text COLLATE "default",
CONSTRAINT "chart_pkey" PRIMARY KEY ("id"),
CONSTRAINT "chart_dash_id_fkey" FOREIGN KEY ("dash_id") REFERENCES "public"."dashboard" ("id") ON DELETE CASCADE ON UPDATE NO ACTION,
CONSTRAINT "chart_table_id_fkey" FOREIGN KEY ("table_id") REFERENCES "public"."table" ("id") ON DELETE RESTRICT ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);
COMMENT ON TABLE "public"."chart" IS '图表';
COMMENT ON COLUMN "public"."chart"."id" IS '图表id';
COMMENT ON COLUMN "public"."chart"."name" IS '图表名称';
COMMENT ON COLUMN "public"."chart"."table_id" IS '图表所用表格';
COMMENT ON COLUMN "public"."chart"."owner" IS '图表所有者';
COMMENT ON COLUMN "public"."chart"."create_time" IS '图表创建时间';
COMMENT ON COLUMN "public"."chart"."modify_time" IS '图表修改时间';
COMMENT ON COLUMN "public"."chart"."web_data" IS '前端数据';
CREATE UNIQUE INDEX "chart_name_owner_idx" ON "public"."chart" USING btree ("name", "owner", "dash_id");

CREATE TABLE "public"."dash_filter" (
"id" text COLLATE "default",
"name" varchar(255) COLLATE "default",
"field_key" varchar(255) COLLATE "default",
"chart_id" varchar(255) COLLATE "default",
CONSTRAINT "dash_filter_chart_id_fkey" FOREIGN KEY ("chart_id") REFERENCES "public"."chart" ("id") ON DELETE CASCADE ON UPDATE NO ACTION,
CONSTRAINT "dash_filter_field_key_fkey" FOREIGN KEY ("field_key") REFERENCES "public"."field" ("id") ON DELETE RESTRICT ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;

CREATE TABLE "public"."dash_link" (
"dash_id" text COLLATE "default" NOT NULL,
"chart_id" text COLLATE "default" NOT NULL,
"field_key" text COLLATE "default" NOT NULL,
"link_chart_id" text COLLATE "default" NOT NULL,
"link_field_key" text COLLATE "default" NOT NULL,
CONSTRAINT "dash_link_dash_id_fkey" FOREIGN KEY ("dash_id") REFERENCES "public"."dashboard" ("id") ON DELETE CASCADE ON UPDATE NO ACTION,
CONSTRAINT "dash_link_chart_id_fkey" FOREIGN KEY ("chart_id") REFERENCES "public"."chart" ("id") ON DELETE CASCADE ON UPDATE NO ACTION,
CONSTRAINT "dash_link_link_chart_id_fkey" FOREIGN KEY ("link_chart_id") REFERENCES "public"."chart" ("id") ON DELETE CASCADE ON UPDATE NO ACTION,
CONSTRAINT "dash_link_field_key_fkey" FOREIGN KEY ("field_key") REFERENCES "public"."field" ("id") ON DELETE RESTRICT ON UPDATE NO ACTION,
CONSTRAINT "dash_link_link_field_key_fkey" FOREIGN KEY ("link_field_key") REFERENCES "public"."field" ("id") ON DELETE RESTRICT ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;

CREATE TABLE "public"."chart_inner_filter" (
"chart_id" text COLLATE "default" NOT NULL,
"field_key" text COLLATE "default" NOT NULL,
CONSTRAINT "chart_inner_filter_chart_id_fkey" FOREIGN KEY ("chart_id") REFERENCES "public"."chart" ("id") ON DELETE CASCADE ON UPDATE NO ACTION,
CONSTRAINT "chart_inner_filter_field_key_fkey" FOREIGN KEY ("field_key") REFERENCES "public"."field" ("id") ON DELETE RESTRICT ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;

CREATE TABLE "public"."dash_element" (
"id" text COLLATE "default" NOT NULL,
"type" "public"."dash_element_type" NOT NULL,
"x" int4 NOT NULL,
"y" int4 NOT NULL,
"w" int4 NOT NULL,
"h" int4 NOT NULL,
"settings" jsonb,
"dash_id" varchar(255) COLLATE "default" NOT NULL,
CONSTRAINT "dash_element_dash_id_fkey" FOREIGN KEY ("dash_id") REFERENCES "public"."dashboard" ("id") ON DELETE CASCADE ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;

CREATE TABLE "public"."chart_field" (
"uniq_id" text COLLATE "default" NOT NULL,
"field_key" text COLLATE "default" NOT NULL,
"alias" text COLLATE "default" NOT NULL,
"operator" text COLLATE "default",
"sort" "public"."field_sort_type" NOT NULL,
"chart_id" text COLLATE "default" NOT NULL,
"level" int4 NOT NULL,
"type" "public"."chart_meta_type",
"order" int4 DEFAULT 0,
CONSTRAINT "chart_field_chart_id_fkey" FOREIGN KEY ("chart_id") REFERENCES "public"."chart" ("id") ON DELETE CASCADE ON UPDATE NO ACTION,
CONSTRAINT "chart_field_field_key_fkey" FOREIGN KEY ("field_key") REFERENCES "public"."field" ("id") ON DELETE RESTRICT ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;

CREATE TABLE "public"."share_dash" (
"id" text COLLATE "default" NOT NULL,
"dash_id" text COLLATE "default" NOT NULL,
CONSTRAINT "share_dashboard_pkey" PRIMARY KEY ("id"),
CONSTRAINT "share_dashboard_dash_id_fkey" FOREIGN KEY ("dash_id") REFERENCES "public"."dashboard" ("id") ON DELETE CASCADE ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;

CREATE TABLE "public"."user_public_data" (
"id" text COLLATE "default",
"owner" text COLLATE "default",
"create_time" timestamp(6)
)
WITH (OIDS=FALSE)
;

CREATE TABLE "public"."ds_dir_ref" (
"ds_id" text COLLATE "default",
"dir_id" text COLLATE "default",
"owner" text COLLATE "default",
FOREIGN KEY ("dir_id") REFERENCES "public"."dir" ("id") ON DELETE CASCADE ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;

CREATE TABLE "public"."ele_del_ref" (
"owner" varchar(255) COLLATE "default",
"type" "public"."bi_ele_type",
"id" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)
;


-- 比对分析
CREATE TYPE node_type AS ENUM ('TABLE', 'RESULT');
CREATE TYPE comp_type AS ENUM ('JOIN', 'FULL_JOIN', 'LEFT_SUBTRACT', 'RIGHT_SUBTRACT');
CREATE TYPE status_type AS ENUM ('SUCCESS', 'RUNNING', 'FAILED', 'NOT_RUN');
CREATE TYPE schedule_status AS ENUM ('ONLINE', 'OFFLINE');

CREATE TABLE "public"."comp_task" (
  "id" text COLLATE "default" NOT NULL,
  "name" varchar(255) COLLATE "default",
  "owner" varchar(255) COLLATE "default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  CONSTRAINT "comp_task_pkey" PRIMARY KEY ("id")
)
WITH (OIDS=FALSE)
;


COMMENT ON COLUMN "public"."comp_task"."name" IS '任务名称';

COMMENT ON COLUMN "public"."comp_task"."owner" IS '责任人';

COMMENT ON COLUMN "public"."comp_task"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."comp_task"."modify_time" IS '修改时间';


CREATE TABLE "public"."com_node" (
  "id" text COLLATE "default" NOT NULL,
  "name" varchar(255) COLLATE "default",
  "type" "public"."node_type",
  "table_id" text COLLATE "default",
  "task_id" text COLLATE "default",
  "position" jsonb,
  CONSTRAINT "com_node_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "com_node_table_id_fkey" FOREIGN KEY ("table_id") REFERENCES "public"."table" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "com_node_task_id_fkey" FOREIGN KEY ("task_id") REFERENCES "public"."comp_task" ("id") ON DELETE CASCADE ON UPDATE CASCADE
)
WITH (OIDS=FALSE)
;


COMMENT ON COLUMN "public"."com_node"."type" IS '节点类型';

COMMENT ON COLUMN "public"."com_node"."table_id" IS '节点为表时的表id';

COMMENT ON COLUMN "public"."com_node"."task_id" IS '比对任务编号';


CREATE TABLE "public"."comp_relation" (
  "task_id" text COLLATE "default",
  "left_id" text COLLATE "default",
  "right_id" text COLLATE "default",
  "type" "public"."comp_type",
  "conditions" jsonb,
  "filter" jsonb,
  "left_cols" jsonb,
  "right_cols" jsonb,
  "result_id" text COLLATE "default",
  CONSTRAINT "comp_relation_task_id_fkey" FOREIGN KEY ("task_id") REFERENCES "public"."comp_task" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "comp_relation_left_id_fkey" FOREIGN KEY ("left_id") REFERENCES "public"."com_node" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "comp_relation_right_id_fkey" FOREIGN KEY ("right_id") REFERENCES "public"."com_node" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;


COMMENT ON COLUMN "public"."comp_relation"."task_id" IS '任务编号';

COMMENT ON COLUMN "public"."comp_relation"."left_id" IS '左节点id';

COMMENT ON COLUMN "public"."comp_relation"."right_id" IS '右节点id';

COMMENT ON COLUMN "public"."comp_relation"."type" IS '计算公式';

COMMENT ON COLUMN "public"."comp_relation"."conditions" IS '参比字段设置';

COMMENT ON COLUMN "public"."comp_relation"."left_cols" IS '左表结果字段';

COMMENT ON COLUMN "public"."comp_relation"."right_cols" IS '右表结果字段';

COMMENT ON COLUMN "public"."comp_relation"."result_id" IS '结果集id';


CREATE TABLE "public"."comp_result" (
  "id" text COLLATE "default" NOT NULL,
  "exec_time" timestamp(6),
  "finished_time" timestamp(6),
  "task_id" text COLLATE "default",
  "result_table" jsonb,
  "status" "public"."status_type",
  CONSTRAINT "comp_result_pkey" PRIMARY KEY ("id")
)
WITH (OIDS=FALSE)
;


COMMENT ON COLUMN "public"."comp_result"."exec_time" IS '执行开始时间';

COMMENT ON COLUMN "public"."comp_result"."finished_time" IS '执行结束时间';

COMMENT ON COLUMN "public"."comp_result"."task_id" IS '任务id';

COMMENT ON COLUMN "public"."comp_result"."result_table" IS '结果表信息';

COMMENT ON COLUMN "public"."comp_result"."status" IS '运行状态';


CREATE TABLE "public"."comp_schedule" (
"task_id" text COLLATE "default",
"create_time" date,
"status" "public"."schedule_status",
"warn_email" text COLLATE "default",
"crontab" jsonb
)
WITH (OIDS=FALSE)
;