function [q_val, v_val] = failed_update(q_val, v_val, pre_state, pre_action, reinf, predicted_value)
global ALPHA GAMMA
q_val(pre_state,pre_action) = q_val(pre_state,pre_action)+ ALPHA*(reinf+ GAMMA*predicted_value - q_val(pre_state,pre_action));
end