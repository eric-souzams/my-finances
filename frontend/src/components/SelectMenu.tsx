import { ChangeEventHandler } from "react";

type SelectProps = {
  label: string;
  value: string;
}

type OptionsProps = {
  id: string;
  value: string;
  onChange: ChangeEventHandler<HTMLSelectElement>;
  className: string;
  list: SelectProps[];
  name?: string;
}

export function SelectMenu(props: OptionsProps) {
  const options = props.list.map((option, index) => {
    return (
      <option key={index} value={option.value}>{option.label}</option>
    );
  })

  return (
    <select {...props}>
      {options}
    </select>
  );
}