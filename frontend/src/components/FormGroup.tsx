import { ReactNode } from "react";

type FormGroupProps = {
    label: string;
    children?: ReactNode;
    htmlFor: string;
    
}

export function FormGroup(props: FormGroupProps) {
    return (
        <div className="form-group">
            <label htmlFor={props.htmlFor}>
                {props.label}
            </label>
            {props.children}
        </div>
    );
}