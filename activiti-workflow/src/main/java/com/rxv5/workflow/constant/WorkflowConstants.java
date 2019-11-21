package com.rxv5.workflow.constant;

public class WorkflowConstants {

    public enum WorkflowState {
        WORKFLOW_CANCEL(0, "取消"), WORKFLOW_RUNNING(1, "审批中"), WORKFLOW_COMPLETE(2, "完成");

        private int id;
        private String text;

        private WorkflowState(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public static WorkflowState byId(int id) {
            WorkflowState ws = null;
            for (WorkflowState state : WorkflowState.values()) {
                if (state.getId() == id) {
                    return state;
                }
            }
            if (ws == null) {
                new RuntimeException("在WorkflowState中没有找见id为{" + id + "}的枚举！");
            }
            return ws;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public enum YesOrNo {
        YES(1, "是"), NO(0, "否");

        private int id;
        private String text;

        private YesOrNo(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public static YesOrNo byId(int id) {
            YesOrNo ws = null;
            for (YesOrNo state : YesOrNo.values()) {
                if (state.getId() == id) {
                    ws = state;
                    break;
                }
            }
            if (ws == null) {
                new RuntimeException("在YesOrNo中没有找见id为{" + id + "}的枚举！");
            }
            return ws;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
